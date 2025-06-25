package de.sipgate.federmappe.common

import kotlin.time.ExperimentalTime
import kotlin.time.Instant


typealias DecodableTimestamp = StringMap

fun createDecodableTimestamp(seconds: Long, nanoseconds: Int = 0): DecodableTimestamp =
    mapOf(
        "epochSeconds" to seconds,
        "nanosecondsOfSecond" to nanoseconds.toLong(),
    )

@ExperimentalTime
fun createDecodableTimestamp(instant: Instant): DecodableTimestamp =
    mapOf(
        "epochSeconds" to instant.epochSeconds,
        "nanosecondsOfSecond" to instant.nanosecondsOfSecond.toLong(),
    )
