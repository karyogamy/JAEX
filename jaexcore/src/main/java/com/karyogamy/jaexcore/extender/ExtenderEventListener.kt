package com.karyogamy.jaexcore.extender

import com.karyogamy.jaexcore.playqueue.PlayQueue
import com.karyogamy.jaexcore.stream.StreamSourceResolver
import java.io.Serializable

interface ExtenderEventListener<QueueMeta : Serializable, ResolvedMeta : Serializable> {

    fun onInitialize(playQueue: PlayQueue<QueueMeta>,
                     resolver: StreamSourceResolver<QueueMeta, ResolvedMeta>)

    fun onPlayQueueRenewal(playQueue: PlayQueue<QueueMeta>)

    fun onResolverRenewal(resolver: StreamSourceResolver<QueueMeta, ResolvedMeta>)

    fun onDispose()
}