package com.karyogamy.jaexcore.playback

import java.io.Serializable
import kotlin.properties.Delegates

abstract class RollingWindowMediaSourceManager<QM : Serializable, RM : Serializable>
    : MediaSourceManager<QM, RM> {

}