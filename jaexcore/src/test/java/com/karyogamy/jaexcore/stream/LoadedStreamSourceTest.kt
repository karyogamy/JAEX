package com.karyogamy.jaexcore.stream

import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.karyogamy.jaexcore.playqueue.FakePlayQueueItem
import com.karyogamy.jaexcore.source.LoadedMediaSource
import junit.framework.Assert.assertTrue
import org.junit.Test

class LoadedStreamSourceTest {
    @Test
    fun `Constructor available for legal MediaSource and metadata`() {
        val legalMediaSource = MergingMediaSource()
        val someMetadata = "some metadata"
        LoadedStreamSource(legalMediaSource, someMetadata)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Multi-windowed MediaSources are not allowed for ConcatenatingMediaSource`() {
        val illegalMediaSource = ConcatenatingMediaSource()
        val someMetadata = "concatenating metadata"
        LoadedStreamSource(illegalMediaSource, someMetadata)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Multi-windowed MediaSources are not allowed for DynamicConcatenatingMediaSource`() {
        val illegalMediaSource = DynamicConcatenatingMediaSource()
        val someMetadata = "dynamic concatenating metadata"
        LoadedStreamSource(illegalMediaSource, someMetadata)
    }

    @Test
    fun `Is morphable into LoadedMediaSource`() {
        val fakePlayQueueItem = FakePlayQueueItem("some item")

        val fakeMediaSource = MergingMediaSource()
        val fakeMetadata = "some metadata"
        val managedMediaSource = LoadedStreamSource(fakeMediaSource, fakeMetadata)
                .toManagedMediaSource(fakePlayQueueItem)

        assertTrue(managedMediaSource is LoadedMediaSource)
        assertTrue(managedMediaSource.getPlayQueueItem() is FakePlayQueueItem)
    }
}