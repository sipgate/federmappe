package de.sipgate.federmappe.common.serializers

import de.sipgate.federmappe.common.StringMapToObjectDecoder
import de.sipgate.federmappe.common.createDecodableTimestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


class TimestampToDateSerializerTest {
    private val epochSeconds = 1707833611L * 1000
    private val date = Date(epochSeconds)

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testDeserialization() {
        @Serializable
        data class TestClass(val a: @Serializable(with = TimestampToDateSerializer::class) Date)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to createDecodableTimestamp(1707833611))

        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(date, result.a)
        assertIs<TestClass>(result)
    }
}
