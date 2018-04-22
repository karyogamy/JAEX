package com.karyogamy.jaexcore.playqueue

import io.reactivex.Flowable
import java.io.Serializable

interface PlayQueue<M : Serializable> {
    /**
     * Returns an immutable current view of the play queue playlist.
     * */
    fun view() : List<PlayQueueItem<M>>

    /**
     * Returns the play queue index which should be played at the moment.
     * */
    fun index() : Int

    /**
     * Changes the current playing index to a new index.
     *
     * This method is guarded using in a circular manner for index exceeding the play queue size.
     *
     * This method is thread-safe and will emit a [SelectEvent] if the index is not
     * the current playing index.
     * */
    fun select(index: Int)

    /**
     * Appends the given [PlayQueueItem]s to the current play queue.
     *
     * If the play queue is shuffled, then append the items to the backup queue as is and
     * append the shuffle items to the play queue.
     *
     * This method is thread-safe and will emit a [AppendEvent] on any given context.
     * */
    fun append(vararg items: PlayQueueItem<M>)

    /**
     * Removes the item at the given indices from the play queue.
     *
     * The current playing index will decrement if it is greater than the index being removed.
     * On cases where the current playing index exceeds the playlist range, it is set to 0.
     *
     * This method is thread-safe and will emit a [RemoveEvent] if the index is within
     * the play queue index range.
     * */
    fun remove(vararg indices: Int)

    /**
     * Moves a queue item at the source index to the target index.
     *
     * If the item being moved is the currently playing, then the current playing index is set
     * to that of the target.
     *
     * If the moved item is not the currently playing and moves to an index <b>AFTER</b> the
     * current playing index, then the current playing index is decremented. Vice versa if
     * the an item after the currently playing is moved <b>BEFORE</b>.
     *
     * This method is thread-safe and will emit a [MoveEvent] if [source] does not equal
     * to [target] or out-of-bound.
     * */
    fun move(source: Int, target: Int)

    /**
     * Reorders the current play queue with a method which produces an ordering given the
     * existing playlist view. The [order] method will be run on the same thread as the
     * caller of this method, thus if the ordering method is costly, this method should be
     * called on a separate thread.
     *
     * The existing playlist will be backed up and the reordered view will be produces.
     *
     * This method is thread-safe and will emit a [ReorderEvent] in any context when called.
     * */
    fun reorder(order: (streams: List<PlayQueueItem<M>>, index: Int) -> Iterable<Int>)

    /**
     * Unorders the current play queue if it has an existing reordering.
     *
     * This method will modify the queue index to the current playing item in the backup
     * playlist view if found, otherwise, the index will reset to 0.
     *
     * This method will emit a [ReorderEvent] if a reordering exists.
     * */
    fun unorder()

    /**
     * Returns true if the current play queue is reordered.
     * */
    fun isReordered() : Boolean

    /**
     * Returns the play queue message bus, which broadcasts all its modification events.
     * */
    fun messageBus() : Flowable<PlayQueueEvent>
}