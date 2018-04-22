package com.karyogamy.jaexcore.extender

import com.google.android.exoplayer2.ExoPlayer
import com.karyogamy.jaexcore.playqueue.PlayQueue
import com.karyogamy.jaexcore.stream.StreamSourceResolver
import java.io.Serializable

abstract class ExoPlayerExtender<QM, RM>(private val exoPlayer: ExoPlayer) :
        BaseExtender<QM, RM> where QM : Serializable, RM : Serializable {

}