package com.karyogamy.jaexcore.playback

import com.google.android.exoplayer2.source.MediaSource
import com.karyogamy.jaexcore.playqueue.PlayQueue
import com.karyogamy.jaexcore.playqueue.PlayQueueItem
import com.karyogamy.jaexcore.stream.MetadataInspector
import com.karyogamy.jaexcore.stream.StreamSourceResolver
import java.io.Serializable

interface MediaSourceManagerListener<QueueMeta : Serializable, ResolvedMeta : Serializable> {

    /**
     * Returns the [StreamSourceResolver] currently registered to the listener, which
     * should be used to resolve [MediaSource]
     * */
    fun getResolver() : StreamSourceResolver<QueueMeta, ResolvedMeta>

    /**
     * Returns the [MetadataInspector] currently registered to the listener, which should
     * be used to check expiration of [ResolvedMeta]
     * */
    fun getInspector() : MetadataInspector<ResolvedMeta>

    /**
     * Called to check if the currently playing stream is approaching the end of its playback.
     * Implementation should return true when the current playback position is progressing within
     * [timeToEdgeMillis] or less to its playback during.
     *
     * May be called at any time.
     * */
    fun isApproachingPlaybackEdge(timeToEdgeMillis: Long): Boolean

    /**
     * Called when the [MediaSourceManager] is preparing the master [MediaSource] and
     * is not yet ready. Signals to the listener to block the player from playing anything
     * and notify the source is now invalid.
     *
     * May be called at any time.
     * */
    fun onPlaybackBlock()

    /**
     * Called when the [MediaSourceManager] has finished preparing the [masterMediaSource]
     * and is ready for playback. Signals to the listener to resume the player by preparing
     * the newly provided [masterMediaSource].
     *
     * May be called only when the player is blocked.
     * */
    fun onPlaybackUnblock(masterMediaSource: MediaSource)

    /**
     * Called when the [MediaSource] at the [PlayQueue] index has been loaded, regardless of
     * success or failure. Signals to the listener that it may want to synchronize
     * its index and metadata.
     *
     * May be called anytime at any amount once unblock is called.
     * */
    fun onPlayerSynchronize(item: PlayQueueItem<QueueMeta>, metadata: ResolvedMeta)

    /**
     * Called when the play queue can no longer to played or used.
     * Currently, this means the [PlayQueue] is empty and complete.
     * Signals to the listener that it should shutdown.
     *
     * May be called at any time.
     * */
    fun onPlaybackShutdown()
}