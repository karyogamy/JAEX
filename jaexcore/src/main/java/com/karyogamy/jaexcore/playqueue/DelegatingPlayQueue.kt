package com.karyogamy.jaexcore.playqueue

import java.io.Serializable

abstract class DelegatingPlayQueue<M : Serializable>(playQueueData: PlayQueueData<M>)
    : PlayQueue<M> by BasePlayQueue<M>(playQueueData)