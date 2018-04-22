package com.karyogamy.jaexcore.source

import com.google.android.exoplayer2.source.MergingMediaSource
import com.karyogamy.jaexcore.playqueue.FakePlayQueueItem
import junit.framework.Assert.assertTrue
import org.junit.Test

class LoadedMediaSourceTest {
    private val fakeMediaSource = MergingMediaSource()
    private val fakeMetadata = "fakeMetadata"
    private val initPlayQueueItem = FakePlayQueueItem("init")
    private val otherPlayQueueItem = FakePlayQueueItem("other")

    @Test
    fun `queue item should be equal to the queue item with same id`() {
        val loadedMediaSource = LoadedMediaSource(initPlayQueueItem, fakeMediaSource, fakeMetadata)
        assertTrue(loadedMediaSource.isItemEqual(initPlayQueueItem))
    }

    @Test
    fun `queue item should not be equal to item with different id`() {
        val loadedMediaSource = LoadedMediaSource(initPlayQueueItem, fakeMediaSource, fakeMetadata)
        assertTrue(!(initPlayQueueItem.getId() != otherPlayQueueItem.getId() &&
                loadedMediaSource.isItemEqual(otherPlayQueueItem)))
    }
}