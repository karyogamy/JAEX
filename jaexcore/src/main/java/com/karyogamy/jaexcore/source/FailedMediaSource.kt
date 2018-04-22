package com.karyogamy.jaexcore.source

import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaPeriod
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.Allocator
import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import java.io.IOException
import java.io.Serializable


class FailedMediaSource<out Source, out Metadata>(private val item : PlayQueueItem<Source>,
                                                  private val error : Throwable,
                                                  private val metadata : Metadata? = null) :
        ManagedMediaSource<Source, Metadata> where Source : Serializable, Metadata : Serializable {

    override val tag: String = "FailedMediaSource@" + hashCode()

    override fun getPlayQueueItem(): PlayQueueItem<Source>? = item

    override fun getResolvedMetadata(): Metadata? = metadata

    override fun isItemEqual(item: PlayQueueItem<*>): Boolean {
        return this.item.getId() == item.getId()
    }

    override fun prepareSource(player: ExoPlayer?,
                               isTopLevelSource: Boolean,
                               listener: MediaSource.Listener?) {
        Log.e(tag, "Loading failed source: ", this.error)
    }

    override fun createPeriod(id: MediaSource.MediaPeriodId?, allocator: Allocator?) = null

    override fun releaseSource() {
        // Do nothing
    }

    override fun releasePeriod(mediaPeriod: MediaPeriod?) {
        // Do nothing
    }

    override fun maybeThrowSourceInfoRefreshError() = throw IOException(error)
}