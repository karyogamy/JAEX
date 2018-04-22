package com.karyogamy.jaexcore.stream

import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import com.karyogamy.jaexcore.source.FailedMediaSource
import com.karyogamy.jaexcore.source.ManagedMediaSource
import java.io.Serializable

class FailedStreamSource<out M>(private val error : Throwable,
                                private val metadata : M) : StreamSource<M> where M : Serializable {
    fun getError() : Throwable = error

    override fun getMetadata() : M = metadata

    override fun <Q : Serializable> toManagedMediaSource(queueItem: PlayQueueItem<Q>) :
            ManagedMediaSource<Q, M> = FailedMediaSource(queueItem, error, metadata)
}