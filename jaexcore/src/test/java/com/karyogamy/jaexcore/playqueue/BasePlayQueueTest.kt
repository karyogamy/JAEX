package com.karyogamy.jaexcore.playqueue

import com.karyogamy.jaexcore.playqueue.PlayQueueEvent.Companion.APPEND
import com.karyogamy.jaexcore.playqueue.PlayQueueEvent.Companion.INIT
import com.karyogamy.jaexcore.playqueue.PlayQueueEvent.Companion.MOVE
import com.karyogamy.jaexcore.playqueue.PlayQueueEvent.Companion.REMOVE
import com.karyogamy.jaexcore.playqueue.PlayQueueEvent.Companion.REORDER
import com.karyogamy.jaexcore.playqueue.PlayQueueEvent.Companion.SELECT
import io.reactivex.subscribers.TestSubscriber
import junit.framework.Assert.assertTrue
import org.junit.Test

class BasePlayQueueTest {
    private val firstPlayQueueItem = FakePlayQueueItem("first")
    private val secondPlayQueueItem = FakePlayQueueItem("second")
    private val thirdPlayQueueItem = FakePlayQueueItem("third")
    private val inputPlaylist = listOf(firstPlayQueueItem, secondPlayQueueItem, thirdPlayQueueItem)

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when starting at out of range indices`() {
        BasePlayQueue(PlayQueueData(inputPlaylist, startAt = inputPlaylist.size + 1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when reordering contains out of range indices`() {
        BasePlayQueue(PlayQueueData(inputPlaylist, ordering = listOf(inputPlaylist.size + 1)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when constructing play queue with empty playlist`() {
        BasePlayQueue(PlayQueueData<String>(emptyList()))
    }

    @Test
    fun `view should return unordered list of streams with no reordering`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        val view = playQueue.view()
        assertTrue(view.size == inputPlaylist.size)
        for (i in 0 until inputPlaylist.size) {
            assertTrue(view[i].getId() == inputPlaylist[i].getId())
        }
    }

    @Test
    fun `view should return ordered list of streams in reordered order when provided with one`() {
        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, ordering = reordering))
        val view = playQueue.view()
        assertTrue(view.size == inputPlaylist.size)
        for (i in 0 until inputPlaylist.size) {
            assertTrue(view[i].getId() == inputPlaylist[reordering[i]].getId())
        }
    }

    @Test
    fun `isReordered should return true when reordering is not empty and vice versa`() {
        val unorderedPlayQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        assertTrue(!unorderedPlayQueue.isReordered())

        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val reorderedPlayQueue = BasePlayQueue(PlayQueueData(inputPlaylist,
                ordering = reordering))
        assertTrue(reorderedPlayQueue.isReordered())
    }

    @Test
    fun `append should add an item to the end of streams`() {
        val playQueue = BasePlayQueue(PlayQueueData(listOf(firstPlayQueueItem)))
        playQueue.append(secondPlayQueueItem)
        assertTrue(playQueue.view()[1] == secondPlayQueueItem)
    }

    @Test
    fun `append should broadcast an append event`() {
        val playQueue = BasePlayQueue(PlayQueueData(listOf(firstPlayQueueItem)))
        val testSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(testSubscriber)
        playQueue.append(secondPlayQueueItem)

        testSubscriber.assertValueCount(2)
        assertTrue(testSubscriber.values()[0].type == INIT)
        assertTrue(testSubscriber.values()[1].type == APPEND)
    }

    @Test
    fun `append should add an item to both ordered and unordered views`() {
        val playQueue = BasePlayQueue(PlayQueueData(listOf(firstPlayQueueItem),
                ordering = listOf(0)))
        playQueue.append(secondPlayQueueItem)
        val orderedView = playQueue.view()
        assertTrue(orderedView.contains(secondPlayQueueItem))

        playQueue.unorder()
        val unorderedView = playQueue.view()
        assertTrue(unorderedView.contains(secondPlayQueueItem))
    }

    @Test
    fun `queues with reordering should show it is reordered`() {
        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, ordering = reordering))
        assertTrue(playQueue.isReordered())
    }

    @Test
    fun `reorder should reverse stream view when given reverse range`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, startAt = 0))
        playQueue.reorder { streams, _ -> IntRange(0, streams.size - 1).reversed() }
        assertTrue(playQueue.view() == listOf(thirdPlayQueueItem, secondPlayQueueItem,
                firstPlayQueueItem))
        assertTrue(playQueue.index() == playQueue.view().size - 1)
    }

    @Test
    fun `reorder should broadcast a reorder event`() {
        val playQueue = BasePlayQueue(PlayQueueData(listOf(firstPlayQueueItem)))
        val testSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(testSubscriber)
        playQueue.reorder { streams, _ -> IntRange(0, streams.size - 1).shuffled() }

        testSubscriber.assertValueCount(2)
        assertTrue(testSubscriber.values()[0].type == INIT)
        assertTrue(testSubscriber.values()[1].type == REORDER)
    }

    @Test
    fun `unorder should change view to default ordering when queue is initially reordered`() {
        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, ordering = reordering))
        playQueue.unorder()
        assertTrue(playQueue.view() == inputPlaylist)
    }

    @Test
    fun `unorder should change view to default ordering when queue is later reordered`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        playQueue.reorder { streams, _ -> IntRange(0, streams.size - 1).reversed() }
        playQueue.unorder()
        assertTrue(playQueue.view() == inputPlaylist)
    }

    @Test
    fun `unorder should broadcast a reorder event`() {
        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, ordering = reordering))
        val testSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(testSubscriber)
        playQueue.unorder()

        testSubscriber.assertValueCount(2)
        assertTrue(testSubscriber.values()[0].type == INIT)
        assertTrue(testSubscriber.values()[1].type == REORDER)
    }

    @Test
    fun `select should broadcast a select event`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        val testSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(testSubscriber)

        playQueue.select(0)

        testSubscriber.assertValueCount(2)
        assertTrue(testSubscriber.values()[0].type == INIT)
        assertTrue(testSubscriber.values()[1].type == SELECT)
    }

    @Test
    fun `select with index out of bound should be clamped if below 0`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        playQueue.select(Int.MIN_VALUE)
        assertTrue(playQueue.index() == 0)
    }

    @Test
    fun `select with index out of bound should round-robin to remainder when greater than size`() {
        val outOfBoundCount = 2
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        val maxQueueIndex = playQueue.view().size - 1
        playQueue.select(maxQueueIndex + outOfBoundCount)
        // Needed to count from last item, thus the second out of bound item is at index 1
        assertTrue(playQueue.index() == -1 + (outOfBoundCount % playQueue.view().size))
    }

    @Test
    fun `move should broadcast a move event`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        val testSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(testSubscriber)

        playQueue.move(0, 1)

        testSubscriber.assertValueCount(2)
        assertTrue(testSubscriber.values()[0].type == INIT)
        assertTrue(testSubscriber.values()[1].type == MOVE)
    }

    @Test
    fun `move should swap position of first two items`() {
        val expectedView = listOf(secondPlayQueueItem, firstPlayQueueItem, thirdPlayQueueItem)

        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        playQueue.move(0, 1)
        assertTrue(playQueue.view() == expectedView)
    }

    @Test
    fun `move should not do anything if source and target indices are the same`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        val testSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(testSubscriber)

        playQueue.move(0, 0)

        assertTrue(playQueue.view() == inputPlaylist)
        testSubscriber.assertValueCount(1)
        assertTrue(testSubscriber.values()[0].type == INIT)
    }

    @Test
    fun `move should not affect original stream list if reordering exists`() {
        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, ordering = reordering))

        playQueue.move(0, 1)
        playQueue.unorder()
        assertTrue(playQueue.view() == inputPlaylist)
    }

    @Test
    fun `move should not do anything if any input index is out of bound`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        playQueue.move(Int.MIN_VALUE, Int.MAX_VALUE)
        assertTrue(playQueue.view() == inputPlaylist)
    }

    @Test
    fun `move should change queue index to target index if it is the source index`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, startAt = 0))
        playQueue.move(0, 1)
        assertTrue(playQueue.index() == 1)
    }

    @Test
    fun `move should decrement queue index if greater than source and less than or equal to target indices`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, startAt = 1))
        playQueue.move(0, 1)
        assertTrue(playQueue.index() == 0)
    }

    @Test
    fun `move should increment queue index if less than source and greater than or equal to source indices`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, startAt = 1))
        playQueue.move(2, 1)
        assertTrue(playQueue.index() == 2)
    }

    @Test
    fun `remove should broadcast a remove event`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        val testSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(testSubscriber)

        playQueue.remove(0)

        assertTrue(playQueue.view().size == inputPlaylist.size - 1)
        testSubscriber.assertValueCount(2)
        assertTrue(testSubscriber.values()[0].type == INIT)
        assertTrue(testSubscriber.values()[1].type == REMOVE)
    }

    @Test
    fun `remove should not do anything if input indices are out of bound`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        playQueue.remove(Int.MAX_VALUE, Int.MIN_VALUE)
        assertTrue(playQueue.view() == inputPlaylist)
    }

    @Test
    fun `remove should remove first item in play queue`() {
        val expectedView = listOf(secondPlayQueueItem, thirdPlayQueueItem)

        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        playQueue.remove(0)

        assertTrue(playQueue.view() == expectedView)
    }

    @Test
    fun `remove should remove the first and last items in the play queue`() {
        val expectedView = listOf(secondPlayQueueItem)

        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist))
        playQueue.remove(0, 2)

        assertTrue(playQueue.view() == expectedView)
    }

    @Test
    fun `remove should decrement queue index when item with lower index is removed`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, startAt = 1))
        playQueue.remove(0)
        assertTrue(playQueue.index() == 0)
    }

    @Test
    fun `remove should set queue index to zero when queue and remove index point to last queue item`() {
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, startAt = inputPlaylist.size - 1))
        playQueue.remove(playQueue.view().size - 1)
        assertTrue(playQueue.index() == 0)
    }

    @Test
    fun `remove should affect both original and reordered streams if reordering exists`() {
        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val playQueue = BasePlayQueue(PlayQueueData(inputPlaylist, ordering = reordering))

        playQueue.remove(0)

        assertTrue(playQueue.view().size == inputPlaylist.size - 1)
        playQueue.unorder()
        assertTrue(playQueue.view().size == inputPlaylist.size - 1)
    }

    @Test
    fun `data should return a replicate of input data`() {
        val reordering = IntRange(0, inputPlaylist.size - 1).shuffled()
        val playQueueData = PlayQueueData(inputPlaylist, startAt = inputPlaylist.size - 1,
                ordering = reordering)
        val playQueue = BasePlayQueue(playQueueData)

        assertTrue(playQueue.data() == playQueueData)
    }

    @Test
    fun `data should return different data object once play queue is modified`() {
        val playQueueData = PlayQueueData(inputPlaylist)
        val playQueue = BasePlayQueue(playQueueData)

        playQueue.reorder { streams, _ -> IntRange(0, streams.size - 1).shuffled() }

        assertTrue(playQueue.data() != playQueueData)
    }

    @Test
    fun `message bus should only emit events occurred after subscription`() {
        val playQueue = BasePlayQueue(PlayQueueData(listOf(firstPlayQueueItem)))
        val earlySubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(earlySubscriber)
        playQueue.append()

        val lateSubscriber = TestSubscriber<PlayQueueEvent>()
        playQueue.messageBus().subscribe(lateSubscriber)
        playQueue.append()

        val earlySubscriberMessageCount = earlySubscriber.valueCount()
        val lateSubscriberMessageCount = lateSubscriber.valueCount()
        assertTrue(earlySubscriberMessageCount > lateSubscriberMessageCount)
    }
}