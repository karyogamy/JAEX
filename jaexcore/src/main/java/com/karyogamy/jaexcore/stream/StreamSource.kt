package com.karyogamy.jaexcore.stream

import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import com.karyogamy.jaexcore.source.ManagedMediaSource
import java.io.Serializable

interface StreamSource<out M> where M : Serializable {
    fun getMetadata() : M

    fun <Q : Serializable> toManagedMediaSource(queueItem: PlayQueueItem<Q>)
            : ManagedMediaSource<Q, M>
}