package com.karyogamy.jaexcore.source

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaPeriod
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.Allocator
import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import java.io.Serializable

class LoadedMediaSource<out Source, out Metadata>(private val item: PlayQueueItem<Source>,
                                                  private val mediaSource: MediaSource,
                                                  private val metadata: Metadata) :
        ManagedMediaSource<Source, Metadata> where Source : Serializable, Metadata : Serializable {
    override val tag: String = "LoadedMediaSource@" + hashCode()

    override fun getPlayQueueItem(): PlayQueueItem<Source>? = item

    override fun getResolvedMetadata(): Metadata? = metadata

    override fun isItemEqual(item: PlayQueueItem<*>): Boolean = this.item.getId() == item.getId()

    override fun prepareSource(player: ExoPlayer?, isTopLevelSource: Boolean,
                               listener: MediaSource.Listener?) {
        mediaSource.prepareSource(player, isTopLevelSource, listener)
    }

    override fun createPeriod(id: MediaSource.MediaPeriodId?, allocator: Allocator?): MediaPeriod {
        return mediaSource.createPeriod(id, allocator)
    }

    override fun releaseSource() {
        mediaSource.releaseSource()
    }

    override fun releasePeriod(mediaPeriod: MediaPeriod?) {
        mediaSource.releasePeriod(mediaPeriod)
    }

    override fun maybeThrowSourceInfoRefreshError() {
        mediaSource.maybeThrowSourceInfoRefreshError()
    }
}