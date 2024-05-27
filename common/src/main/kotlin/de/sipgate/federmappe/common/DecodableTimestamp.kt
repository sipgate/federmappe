package de.sipgate.federmappe.common


typealias DecodableTimestamp = Map<String, Any>

fun createDecodableTimestamp(seconds: Long, nanoseconds: Int = 0): DecodableTimestamp =
    mapOf(
        "epochSeconds" to seconds,
        "nanosecondsOfSecond" to nanoseconds.toLong(),
    )


