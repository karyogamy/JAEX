package com.karyogamy.jaexcore.stream

import com.karyogamy.jaexcore.handler.Resolver
import java.io.Serializable

interface StreamSourceResolver<in Source, out Metadata> : Resolver<Source, StreamSource<Metadata>>
        where Source : Serializable, Metadata : Serializable