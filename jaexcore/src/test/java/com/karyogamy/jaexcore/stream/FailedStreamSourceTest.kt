package com.karyogamy.jaexcore.stream

import com.karyogamy.jaexcore.playqueue.FakePlayQueueItem
import com.karyogamy.jaexcore.source.FailedMediaSource
import junit.framework.Assert.assertTrue
import org.junit.Test

class FailedStreamSourceTest {
    @Test
    fun `Is morphable into FailedMediaSource`() {
        val fakePlayQueueItem = FakePlayQueueItem("some item")

        val fakeError = IllegalArgumentException()
        val fakeMetadata = "some metadata"
        val managedMediaSource = FailedStreamSource(fakeError, fakeMetadata)
                .toManagedMediaSource(fakePlayQueueItem)

        assertTrue(managedMediaSource is FailedMediaSource)
        assertTrue(managedMediaSource.getPlayQueueItem() is FakePlayQueueItem)
    }
}