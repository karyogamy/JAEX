package com.karyogamy.jaexcore.stream

import com.karyogamy.jaexcore.handler.Inspector
import java.io.Serializable

interface MetadataInspector<in Metadata> : Inspector<Metadata> where Metadata : Serializable