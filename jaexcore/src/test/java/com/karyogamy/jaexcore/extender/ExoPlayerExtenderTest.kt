package com.karyogamy.jaexcore.extender

import com.google.android.exoplayer2.ExoPlayer
import com.karyogamy.jaexcore.playqueue.BasePlayQueue
import com.karyogamy.jaexcore.playqueue.FakePlayQueueItem
import com.karyogamy.jaexcore.playqueue.PlayQueueData
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

class ExoPlayerExtenderTest {
    private val fakePlayer = FakeExoPlayer()
    private val fakeResolver = FakeResolver()

    private val firstPlayQueueItem = FakePlayQueueItem("first")
    private val secondPlayQueueItem = FakePlayQueueItem("second")
    private val thirdPlayQueueItem = FakePlayQueueItem("third")
    private val inputPlaylist = listOf(firstPlayQueueItem, secondPlayQueueItem, thirdPlayQueueItem)
    private val fakePlayQueue = BasePlayQueue(PlayQueueData(inputPlaylist))

}