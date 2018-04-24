package com.karyogamy.jaexcore.playback

import com.karyogamy.jaexcore.playqueue.PlayQueue
import com.karyogamy.jaexcore.playqueue.PlayQueueEvent
import java.io.Serializable

interface MediaSourceManager<QueueMeta : Serializable, ResolvedMeta : Serializable> {
    fun onPlayQueueEvent(playQueueEvent: PlayQueueEvent)

    fun getPlayQueue() : PlayQueue<QueueMeta>

    fun getMediaSourceManagerListener() : MediaSourceManagerListener<QueueMeta, ResolvedMeta>
}