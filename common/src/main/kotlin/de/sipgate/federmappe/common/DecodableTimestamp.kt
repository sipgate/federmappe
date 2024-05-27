package de.sipgate.federmappe.common


typealias DecodableTimestamp = Map<String, Any>

fun createDecodableTimestamp(seconds: Long, nanoseconds: Int): DecodableTimestamp =
    mapOf(
        "epochSeconds" to seconds,
        "nanosecondsOfSecond" to nanoseconds.toLong(),
    )


