package com.karyogamy.jaexcore.playqueue

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.io.Serializable
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.properties.Delegates

class BasePlayQueue<M : Serializable>(data: PlayQueueData<M>) : PlayQueue<M> {
    private var streams: MutableList<PlayQueueItem<M>> by Delegates.notNull()
    private var reorderedStreams: MutableList<PlayQueueItem<M>> by Delegates.notNull()
    private var queueIndex: AtomicInteger by Delegates.notNull()

    private val eventBroadcast: Subject<PlayQueueEvent>
    private val messageBus: Flowable<PlayQueueEvent>

    init {
        if (data.playlist.isEmpty()) {
            throw IllegalArgumentException("playlist must be non-empty.")
        }

        if (data.ordering.max() ?: 0 >= data.playlist.size || data.ordering.min() ?: 0 < 0) {
            throw IllegalArgumentException("ordering must only contain queueIndex within bounds.")
        }

        if (data.startAt < 0 || data.startAt >= data.playlist.size) {
            throw IllegalArgumentException("starting index must be within bound:[${data.startAt}]")
        }

        streams = data.playlist.toMutableList()
        reorderedStreams = streamsFromOrdering(streams, data.ordering).toMutableList()
        queueIndex = AtomicInteger(data.startAt)

        eventBroadcast = PublishSubject.create()
        messageBus = eventBroadcast.toFlowable(BackpressureStrategy.DROP)
                .startWith(InitEvent(view().size))
    }

    override fun view(): List<PlayQueueItem<M>> {
        return mutableView()
    }

    override fun index(): Int = queueIndex.get()

    @Synchronized
    override fun select(index: Int) {
        val currentQueueIndex = index()

        var newIndex = index
        if (newIndex < 0) newIndex = 0
        if (newIndex > view().size) newIndex = index % view().size

        val newQueueIndex = newIndex
        queueIndex.set(newQueueIndex)
        broadcast(SelectEvent(oldIndex = currentQueueIndex, newIndex = newQueueIndex))
    }

    @Synchronized
    override fun append(vararg items: PlayQueueItem<M>) {
        streams.addAll(items)

        if (reorderedStreams.isNotEmpty()) {
            reorderedStreams.addAll(items)
        }

        broadcast(AppendEvent(amount = items.size))
    }

    @Synchronized
    override fun remove(vararg indices: Int) {
        val size = view().size
        val indicesToRemove = indices.sortedArrayDescending().filter { it in 0..(size - 1) }

        indicesToRemove.map { removeInternal(it) }

        broadcast(RemoveEvent(removedIndices = indicesToRemove, selectIndex = index()))
    }

    private fun removeInternal(removeIndex: Int) {
        val size = view().size
        if (removeIndex < 0 || removeIndex >= size) return

        val currentQueueIndex = index()
        if (currentQueueIndex > removeIndex) {
            queueIndex.decrementAndGet()
        } else if (currentQueueIndex == removeIndex && currentQueueIndex == size - 1) {
            queueIndex.set(0)
        }

        val currentQueueItem = view()[removeIndex]
        streams.remove(currentQueueItem)
        reorderedStreams.remove(currentQueueItem)
    }

    @Synchronized
    override fun move(source: Int, target: Int) {
        val listToModify = mutableView()

        if (source == target) return
        if (source < 0 || target < 0) return
        if (source >= listToModify.size || target >= listToModify.size) return

        when (index()) {
            source -> queueIndex.set(target)
            // these IntProgression are valid only when right side is greater than left side
            in (source + 1)..target -> queueIndex.decrementAndGet()
            in target..(source - 1) -> queueIndex.incrementAndGet()
        }

        listToModify.add(target, listToModify.removeAt(source))
        broadcast(MoveEvent(fromIndex = source, toIndex = target))
    }

    @Synchronized
    override fun reorder(order: (streams: List<PlayQueueItem<M>>, index: Int) -> Iterable<Int>) {
        val currentQueueIndex = index()
        val newOrdering = order(streams, currentQueueIndex)
        val newQueueIndex = max(newOrdering.indexOf(currentQueueIndex), /*whenIndexNotFound=*/0)

        reorderedStreams = streamsFromOrdering(streams, newOrdering).toMutableList()
        queueIndex.set(newQueueIndex)

        broadcast(ReorderEvent(oldSelectIndex = currentQueueIndex, newSelectIndex = newQueueIndex))
    }

    @Synchronized
    override fun unorder() {
        if (!isReordered()) return

        val currentQueueIndex = index()
        val currentQueueItem = reorderedStreams[currentQueueIndex]
        val currentQueueItemId = currentQueueItem.getId()
        val newQueueIndex = max(streams.indexOfFirst { it.getId() == currentQueueItemId },
                /*whenIndexNotFound=*/0)

        reorderedStreams.clear()
        queueIndex.set(newQueueIndex)

        broadcast(ReorderEvent(oldSelectIndex = currentQueueIndex, newSelectIndex = newQueueIndex))
    }

    override fun isReordered(): Boolean = reorderedStreams.isNotEmpty()

    override fun messageBus(): Flowable<PlayQueueEvent> = messageBus

    fun data() : PlayQueueData<M> {
        val reordering = orderingFromStreams(streams, reorderedStreams).toList()
        return PlayQueueData(streams, index(), reordering)
    }

    private fun streamsFromOrdering(originalStreams: Iterable<PlayQueueItem<M>>,
                                    ordering: Iterable<Int>) : Iterable<PlayQueueItem<M>> {
        return ordering.map { originalStreams.elementAt(it) }
    }

    private fun orderingFromStreams(originalStreams: Iterable<PlayQueueItem<M>>,
                                    orderedStream: Iterable<PlayQueueItem<M>>) : Iterable<Int> {
        return orderedStream.map { originalStreams.indexOf(it) }
    }

    private fun broadcast(playQueueEvent: PlayQueueEvent) {
        eventBroadcast.onNext(playQueueEvent)
    }

    private fun mutableView(): MutableList<PlayQueueItem<M>> {
        return if (reorderedStreams.isEmpty()) streams else reorderedStreams
    }
}