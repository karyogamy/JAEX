package com.karyogamy.jaexcore.stream

import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import com.karyogamy.jaexcore.source.LoadedMediaSource
import com.karyogamy.jaexcore.source.ManagedMediaSource
import java.io.Serializable

class LoadedStreamSource<out M>(private val mediaSource : MediaSource,
                                private val metadata : M) : StreamSource<M> where M : Serializable {

    init {
        if (mediaSource is ConcatenatingMediaSource ||
                mediaSource is DynamicConcatenatingMediaSource) {
            throw IllegalArgumentException("Multi-windowed MediaSource not supported.")
        }
    }

    fun getMediaSource() : MediaSource = mediaSource

    override fun getMetadata() : M = metadata

    override fun <Q : Serializable> toManagedMediaSource(queueItem: PlayQueueItem<Q>)
            : ManagedMediaSource<Q, M> = LoadedMediaSource(queueItem, mediaSource, metadata)
}