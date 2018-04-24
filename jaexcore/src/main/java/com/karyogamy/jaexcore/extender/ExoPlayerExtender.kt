package com.karyogamy.jaexcore.extender

import com.google.android.exoplayer2.ExoPlayer
import com.karyogamy.jaexcore.playback.MediaSourceManager
import com.karyogamy.jaexcore.playqueue.PlayQueue
import com.karyogamy.jaexcore.stream.StreamSourceResolver
import java.io.Serializable

abstract class ExoPlayerExtender<QM, RM>(private val exoPlayer: ExoPlayer,
                                         private val manager: MediaSourceManager<QM, RM>) :
        BaseExtender<QM, RM> where QM : Serializable, RM : Serializable {
    override fun getPlayer(): ExoPlayer = exoPlayer
    override fun getMediaSourceManager(): MediaSourceManager<QM, RM> = manager
}