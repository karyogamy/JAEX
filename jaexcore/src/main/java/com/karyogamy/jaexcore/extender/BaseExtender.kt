package com.karyogamy.jaexcore.extender

import com.google.android.exoplayer2.ExoPlayer
import com.karyogamy.jaexcore.playqueue.PlayQueue
import com.karyogamy.jaexcore.stream.StreamSourceResolver
import java.io.Serializable

interface BaseExtender<QueueMeta : Serializable, ResolvedMeta : Serializable> {

    fun getPlayer() : ExoPlayer

    fun getCurrentResolvedMetadata() : ResolvedMeta?

    /* Flow Control */

    fun initialize(builder: PlayQueue<QueueMeta>,
                   resolver: StreamSourceResolver<QueueMeta, ResolvedMeta>)

    fun invalidate()

    /* Play Queues */

    fun setPlayQueue(builder: PlayQueue<QueueMeta>)

    fun getPlayQueue() : PlayQueue<QueueMeta>?

    /* Resolvers */

    fun setResolver(resolver: StreamSourceResolver<QueueMeta, ResolvedMeta>)

    fun getResolver() : StreamSourceResolver<QueueMeta, ResolvedMeta>?

    /* Listeners */

    fun addListener(listener: ExtenderEventListener<QueueMeta, ResolvedMeta>)

    fun removeListener(listener: ExtenderEventListener<QueueMeta, ResolvedMeta>)

    fun clearListeners()
}
