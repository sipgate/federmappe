package de.sipgate.federmappe.serializers

import com.google.firebase.Timestamp
import de.sipgate.federmappe.StringMapToObjectDecoder
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class InstantSerializerTest {
    private val instant = Instant.fromEpochSeconds(1707833611, 801)

    @Test
    fun testDeserialization() {
        @Serializable
        data class TestClass(val a: @Serializable(with = InstantSerializer::class) Instant)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to Timestamp(1707833611, 801))

        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        Assertions.assertEquals(instant, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
