package de.sipgate.federmappe.firestore.types

import com.google.firebase.Timestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals

class FirestoreTimestampToDecodableTimestampTest {

    @Test
    fun timestampWithNanosecondPrecisionIsConvertedSuccessfully() {
        val expectedInstant = Instant.fromEpochSeconds(1716823455, 854)

        val timestamp = Timestamp(expectedInstant.toJavaInstant())

        val result = timestamp.toDecodableTimestamp()
        assertEquals(expectedInstant.epochSeconds, result["epochSeconds"])
        assertEquals(expectedInstant.nanosecondsOfSecond.toLong(), result["nanosecondsOfSecond"])
    }

    @Test
    fun timestampWithSecondPrecisionIsConvertedSuccessfully() {
        val expectedDate = Date.from(Instant.fromEpochSeconds(1716823455).toJavaInstant())
        val expectedEpochSeconds = expectedDate.time / 1000

        val timestamp = Timestamp(expectedDate)

        val result = timestamp.toDecodableTimestamp()

        assertEquals(expectedEpochSeconds, result["epochSeconds"])
        assertEquals(0L, result["nanosecondsOfSecond"])
    }
}
