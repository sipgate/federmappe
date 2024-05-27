package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.decoder.StringMapToObjectDecoder
import de.sipgate.federmappe.common.serializers.TimestampToDateSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.serializers.InstantComponentSerializer
import kotlinx.datetime.toInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalSerializationApi::class)
class DecodableTimestampToKotlinInstantTest {

    private val date = createFakeInstant(1)

    private val listDate = listOf(
        createFakeInstant(1),
        createFakeInstant(2),
        createFakeInstant(3),
    )

    @Test
    fun deserializeBasicDataClassWithDateFieldSetToFirstDayOfYear2000() {
        // Arrange
        @Serializable
        data class TestClass(@Contextual val a: Instant)

        val serializer = serializer<TestClass>()

        val data = mapOf<String, Any?>("a" to createDecodableTimestamp(date))

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        // Assert
        assertEquals(date, result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeBasicDataClassWithDateFieldAndFixedSerializer() {
        // Arrange
        @Serializable
        data class TestClass(@Serializable(with = InstantComponentSerializer::class) val a: Instant)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to createDecodableTimestamp(date))

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(date, result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableDateField() {
        // Arrange
        @Serializable
        data class TestClass(@Contextual val a: Instant?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to createDecodableTimestamp(date))

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        // Assert
        assertEquals(date, result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeBasicDataClassWithNullDateField() {
        // Arrange
        @Serializable
        data class TestClass(@Contextual val a: Instant?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to null)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(TimestampToDateSerializer) },
                ),
            )

        // Assert
        assertNull(result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeListOfDate() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<@Contextual Instant>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to listDate.map { createDecodableTimestamp(it) })

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        // Assert
        assertEquals(listDate, result.a)
        assertIs<TestClass>(result)
    }

    private fun createFakeInstant(day: Int) = LocalDateTime(
        year = 2000,
        month = Month.JANUARY,
        dayOfMonth = day,
        hour = 1,
        minute = 1,
        second = 1,
        nanosecond = 0
    ).toInstant(TimeZone.UTC)
}
