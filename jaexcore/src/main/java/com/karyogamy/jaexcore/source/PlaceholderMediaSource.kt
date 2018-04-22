package com.karyogamy.jaexcore.source

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaPeriod
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.Allocator
import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import java.io.Serializable

class PlaceholderMediaSource : ManagedMediaSource<Serializable, Serializable> {
    override val tag: String = "PlaceholderMediaSource@" + hashCode()

    override fun getPlayQueueItem(): PlayQueueItem<Serializable>? = null

    override fun getResolvedMetadata(): Serializable? = null

    override fun isItemEqual(item: PlayQueueItem<*>) = false

    override fun prepareSource(player: ExoPlayer?,
                               isTopLevelSource: Boolean,
                               listener: MediaSource.Listener?) {
        // Do nothing
    }

    override fun createPeriod(id: MediaSource.MediaPeriodId?, allocator: Allocator?) = null

    override fun releaseSource() {
        // Do nothing
    }

    override fun releasePeriod(mediaPeriod: MediaPeriod?) {
        // Do nothing
    }

    override fun maybeThrowSourceInfoRefreshError() {
        // Do nothing
    }
}