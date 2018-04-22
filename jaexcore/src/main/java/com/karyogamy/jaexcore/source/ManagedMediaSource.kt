package com.karyogamy.jaexcore.source

import com.google.android.exoplayer2.source.MediaSource
import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import java.io.Serializable


interface ManagedMediaSource<out Q, out M> : MediaSource where Q : Serializable, M : Serializable {
    val tag: String

    fun getPlayQueueItem() : PlayQueueItem<Q>?

    fun getResolvedMetadata() : M?

    fun isItemEqual(item: PlayQueueItem<*>): Boolean
}