package com.karyogamy.jaexcore.playqueue

import java.io.Serializable

data class PlayQueueData<out M : Serializable>(val playlist: List<PlayQueueItem<M>>,
                                               val startAt: Int = 0,
                                               val ordering: List<Int> = emptyList())