package com.karyogamy.jaexcore.handler

interface Inspector<in Item> {
    fun hasExpired(item : Item): Boolean
}