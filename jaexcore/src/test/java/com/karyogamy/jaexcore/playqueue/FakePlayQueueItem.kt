package com.karyogamy.jaexcore.playqueue

class FakePlayQueueItem(private val name: String,
                        private val data: String = "Some fake data") : PlayQueueItem<String> {
    override fun getTitle(): String = name

    override fun getMetadata(): String = data
}