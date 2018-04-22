package com.karyogamy.jaexcore.source

import com.karyogamy.jaexcore.playqueue.FakePlayQueueItem
import junit.framework.Assert.assertTrue
import org.junit.Test

class PlaceholderMediaSourceTest {
    @Test
    fun `placeholder should never equal to any queue item`() {
        val playQueueItem = FakePlayQueueItem("some item")
        val placeholderMediaSource = PlaceholderMediaSource()
        assertTrue(!placeholderMediaSource.isItemEqual(playQueueItem))
    }
}