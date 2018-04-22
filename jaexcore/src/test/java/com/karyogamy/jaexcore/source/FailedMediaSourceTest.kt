package com.karyogamy.jaexcore.source

import com.karyogamy.jaexcore.playqueue.FakePlayQueueItem
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.junit.Test

class FailedMediaSourceTest {
    private val initError = Throwable("this exception")
    private val fakeMetadata = "fakeMetadata"
    private val initPlayQueueItem = FakePlayQueueItem("init")
    private val otherPlayQueueItem = FakePlayQueueItem("other")

    @Test
    fun `queue item should be equal to the queue item with same id`() {
        val failedMediaSource = FailedMediaSource(initPlayQueueItem, initError, fakeMetadata)
        assertTrue(failedMediaSource.isItemEqual(initPlayQueueItem))
    }

    @Test
    fun `queue item should not be equal to item with different id`() {
        val failedMediaSource = FailedMediaSource(initPlayQueueItem, initError, fakeMetadata)
        assertTrue(!(initPlayQueueItem.getId() != otherPlayQueueItem.getId() &&
                failedMediaSource.isItemEqual(otherPlayQueueItem)))
    }
}