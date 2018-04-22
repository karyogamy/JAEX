package com.karyogamy.jaexcore.handler

interface Resolver<in Source, out Resolution> {
    fun resolveFrom(source: Source): Resolution?
}