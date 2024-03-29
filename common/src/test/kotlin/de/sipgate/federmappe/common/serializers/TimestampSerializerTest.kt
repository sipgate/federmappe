package de.sipgate.federmappe.common.serializers

import de.sipgate.federmappe.common.StringMapToObjectDecoder
import java.util.Date
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class TimestampSerializerTest {
    private val epochSeconds = 1707833611L * 1000
    private val nanos = 801 / 1000000

    /* Nanos will be capped at millisecond precision, because java.util.Date isn't precise enough. */
    private val date = Date(epochSeconds + nanos)

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testDeserialization() {
        @Serializable
        data class TestClass(val a: @Serializable(with = TimestampSerializer::class) Date)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>(
            "a" to mapOf(
                "seconds" to 1707833611L,
                "nanoseconds" to 801L
            )
        )

        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        Assertions.assertEquals(date, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
