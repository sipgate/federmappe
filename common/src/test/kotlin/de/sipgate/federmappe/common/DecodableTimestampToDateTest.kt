package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.serializers.TimestampToDateSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalSerializationApi::class)
class DecodableTimestampToDateTest {

    private val date = LocalDateTime(
        year = 2000,
        month = Month.JANUARY,
        dayOfMonth = 1,
        hour = 1,
        minute = 1,
        second = 1,
        nanosecond = 0
    ).toInstant(TimeZone.UTC)

    private val listDate = listOf(
        LocalDateTime(
            year = 2000,
            month = Month.JANUARY,
            dayOfMonth = 1,
            hour = 1,
            minute = 1,
            second = 1,
            nanosecond = 0
        ).toInstant(TimeZone.UTC),
        LocalDateTime(
            year = 2000,
            month = Month.FEBRUARY,
            dayOfMonth = 1,
            hour = 1,
            minute = 1,
            second = 1,
            nanosecond = 0
        ).toInstant(TimeZone.UTC),
        LocalDateTime(
            year = 2000,
            month = Month.MARCH,
            dayOfMonth = 1,
            hour = 1,
            minute = 1,
            second = 1,
            nanosecond = 0
        ).toInstant(TimeZone.UTC),
    )

    @Test
    fun deserializeBasicDataClassWithDateFieldSetToFirstDayOfYear2000() {
        // Arrange
        @Serializable
        data class TestClass(
            @Contextual val a: Date,
        )

        val serializer = serializer<TestClass>()

        val data =
            mapOf<String, Any?>(
                "a" to createDecodableTimestamp(date.epochSeconds),
            )

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                    serializersModule = SerializersModule { contextual(TimestampToDateSerializer) }
                ),
            )

        // Assert
        assertEquals(Date.from(date.toJavaInstant()), result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeBasicDataClassWithDateFieldAndFixedSerializer() {
        // Arrange
        @Serializable
        data class TestClass(
            @Serializable(with = TimestampToDateSerializer::class) val a: Date,
        )

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to createDecodableTimestamp(date.epochSeconds),
            )

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true
                ),
            )

        // Assert
        assertEquals(Date.from(date.toJavaInstant()), result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableDateField() {
        // Arrange
        @Serializable
        data class TestClass(
            @Contextual val a: Date?,
        )

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to createDecodableTimestamp(date.epochSeconds),
            )

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                    serializersModule = SerializersModule { contextual(TimestampToDateSerializer) }
                ),
            )

        // Assert
        assertEquals(Date.from(date.toJavaInstant()), result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeBasicDataClassWithNullDateField() {
        // Arrange
        @Serializable
        data class TestClass(
            @Contextual val a: Date?,
        )

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to null,
            )

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
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
        data class TestClass(val a: List<@Contextual Date>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listDate.map { createDecodableTimestamp(it.epochSeconds) }

            )

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                    serializersModule = SerializersModule { contextual(TimestampToDateSerializer) }
                ),
            )

        // Assert
        assertEquals(
            listDate.map { Date.from(it.toJavaInstant()) },
            result.a,
        )
        assertIs<TestClass>(result)
    }
}
