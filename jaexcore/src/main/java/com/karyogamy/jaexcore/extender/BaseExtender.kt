package com.karyogamy.jaexcore.extender

import com.google.android.exoplayer2.ExoPlayer
import com.karyogamy.jaexcore.playback.MediaSourceManager
import com.karyogamy.jaexcore.playback.MediaSourceManagerListener
import com.karyogamy.jaexcore.playqueue.PlayQueue
import com.karyogamy.jaexcore.stream.MetadataInspector
import com.karyogamy.jaexcore.stream.StreamSourceResolver
import java.io.Serializable

interface BaseExtender<QueueMeta : Serializable, ResolvedMeta : Serializable>:
        MediaSourceManagerListener<QueueMeta, ResolvedMeta> {

    fun getPlayer() : ExoPlayer

    fun getMediaSourceManager() : MediaSourceManager<QueueMeta, ResolvedMeta>

    fun getCurrentResolvedMetadata() : ResolvedMeta?

    /* Flow Control */

    fun initialize(queue: PlayQueue<QueueMeta>,
                   resolver: StreamSourceResolver<QueueMeta, ResolvedMeta>,
                   inspector: MetadataInspector<ResolvedMeta>)

    fun invalidate()

    /* Play Queues */

    fun setPlayQueue(queue: PlayQueue<QueueMeta>)

    fun getPlayQueue() : PlayQueue<QueueMeta>?

    /* Resolver */

    fun setResolver(resolver: StreamSourceResolver<QueueMeta, ResolvedMeta>)

    /* Inspector */

    fun setInspector(inspector: MetadataInspector<ResolvedMeta>)

    /* Listeners */

    fun addListener(listener: ExtenderEventListener<QueueMeta, ResolvedMeta>)

    fun removeListener(listener: ExtenderEventListener<QueueMeta, ResolvedMeta>)

    fun clearListeners()
}
