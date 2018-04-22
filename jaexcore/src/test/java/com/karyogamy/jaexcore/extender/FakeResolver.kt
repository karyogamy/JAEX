package com.karyogamy.jaexcore.extender

import com.karyogamy.jaexcore.stream.StreamSource
import com.karyogamy.jaexcore.stream.StreamSourceResolver

class FakeResolver : StreamSourceResolver<String, String> {
    override fun resolveFrom(source: String): StreamSource<String>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}