package de.sipgate.federmappe.common

import kotlinx.datetime.Instant


typealias DecodableTimestamp = Map<String, Any>

fun createDecodableTimestamp(seconds: Long, nanoseconds: Int = 0): DecodableTimestamp =
    mapOf(
        "epochSeconds" to seconds,
        "nanosecondsOfSecond" to nanoseconds.toLong(),
    )

fun createDecodableTimestamp(instant: Instant): DecodableTimestamp =
    mapOf(
        "epochSeconds" to instant.epochSeconds,
        "nanosecondsOfSecond" to instant.nanosecondsOfSecond.toLong(),
    )
