package com.karyogamy.jaexcore.playqueue

import java.io.Serializable
import java.util.*

interface PlayQueueItem<out M : Serializable> : Serializable {

    companion object {
        private val id: String = UUID.randomUUID().toString()
    }

    fun getId(): String = id

    fun getTitle(): String

    fun getMetadata(): M
}
