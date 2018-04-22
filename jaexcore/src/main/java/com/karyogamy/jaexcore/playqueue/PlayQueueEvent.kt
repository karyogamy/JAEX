package com.karyogamy.jaexcore.playqueue

import java.io.Serializable

sealed class PlayQueueEvent(val type: Int) : Serializable {
    companion object {
        const val INIT = 0
        const val SELECT = 1
        const val APPEND = 2
        const val REMOVE = 3
        const val MOVE = 4
        const val REORDER = 5
    }
}

data class InitEvent(val currentSize: Int) : PlayQueueEvent(INIT)

data class SelectEvent(val oldIndex: Int, val newIndex: Int) : PlayQueueEvent(SELECT)

data class AppendEvent(val amount: Int) : PlayQueueEvent(APPEND)

data class RemoveEvent(val removedIndices: List<Int>, val selectIndex: Int) : PlayQueueEvent(REMOVE)

data class MoveEvent(val fromIndex: Int, val toIndex: Int) : PlayQueueEvent(MOVE)

data class ReorderEvent(val oldSelectIndex: Int, val newSelectIndex: Int) : PlayQueueEvent(REORDER)
