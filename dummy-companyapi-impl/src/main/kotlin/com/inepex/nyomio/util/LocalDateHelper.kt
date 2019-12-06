package com.inepex.nyomio.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toUtcMillis() = this.toInstant(ZoneOffset.UTC).toEpochMilli()

fun localDateFromMillis(millis: Long) = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC)
