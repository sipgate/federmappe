package de.sipgate.federmappe.firestore.types

import com.google.firebase.Timestamp
import de.sipgate.federmappe.common.decoder.StringMapToObjectDecoder
import de.sipgate.federmappe.common.serializers.InstantComponentSerializer
import de.sipgate.federmappe.firestore.normalizeStringMap
import de.sipgate.federmappe.firestore.normalizeStringMapNullable
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
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@OptIn(ExperimentalTime::class)
class FirestoreTimestampNormalizationTest {

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

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun firestoreTimestampIsDecodedToKotlinInstantCorrectly() {
        // Arrange
        val expectedInstant = Instant.fromEpochSeconds(1716823455)
        val timestamp = mapOf(
            "epochSeconds" to expectedInstant.epochSeconds,
            "nanosecondsOfSecond" to expectedInstant.nanosecondsOfSecond.toLong()
        )

        @Serializable
        data class MockLocalDataClass(
            @Contextual
            val createdAt: Instant
        )

        val serializer = serializer<MockLocalDataClass>()

        val data = mapOf<String, Any?>("createdAt" to timestamp)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        // Assert
        assertEquals(expectedInstant, result.createdAt)
        assertIs<MockLocalDataClass>(result)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun firestoreTimestampIsDecodedToNullableKotlinInstantCorrectly() {
        // Arrange
        val expectedInstant = Instant.fromEpochSeconds(1716823455)
        val timestamp = mapOf(
            "epochSeconds" to expectedInstant.epochSeconds,
            "nanosecondsOfSecond" to expectedInstant.nanosecondsOfSecond.toLong()
        )

        @Serializable
        data class MockLocalDataClass(
            @Contextual
            val createdAt: Instant?
        )

        val serializer = serializer<MockLocalDataClass>()

        val data = mapOf<String, Any?>("createdAt" to timestamp)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data = data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) },
                ),
            )

        // Assert
        assertEquals(expectedInstant, result.createdAt)
        assertIs<MockLocalDataClass>(result)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun nullIsDecodedToNullableKotlinInstantCorrectly() {
        // Arrange
        @Serializable
        data class MockLocalDataClass(
            @Contextual
            val createdAt: Instant?
        )

        val serializer = serializer<MockLocalDataClass>()

        val data = mapOf<String, Any?>("createdAt" to null)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        // Assert
        assertEquals(null, result.createdAt)
        assertIs<MockLocalDataClass>(result)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun nestedFirestoreTimestampIsNormalizedAndDecodedToKotlinInstantCorrectly() {
        // Arrange
        val expectedInstant = Instant.fromEpochSeconds(1716823455)
        val expectedDate = Date.from(expectedInstant.toJavaInstant())
        val timestamp = Timestamp(expectedDate)

        @Serializable
        data class MockNestedDataClass(
            @Contextual
            val createdAt: Instant
        )

        @Serializable
        data class MockLocalDataClass(
            @Contextual
            val nested: MockNestedDataClass
        )

        val serializer = serializer<MockLocalDataClass>()

        val data =
            mapOf<String, Any>("nested" to mapOf<String, Any>("createdAt" to timestamp)).normalizeStringMap()

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        // Assert
        assertEquals(expectedInstant, result.nested.createdAt)
        assertIs<MockLocalDataClass>(result)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun nestedFirestoreTimestampIsNormalizedAndDecodedToKotlinInstantCorrectlyNullable() {
        // Arrange
        val expectedInstant = Instant.fromEpochSeconds(1716823455)
        val expectedDate = Date.from(expectedInstant.toJavaInstant())
        val timestamp = Timestamp(expectedDate)

        @Serializable
        data class MockNestedDataClass(
            @Contextual
            val createdAt: Instant
        )

        @Serializable
        data class MockLocalDataClass(
            @Contextual
            val nested: MockNestedDataClass?
        )

        val serializer = serializer<MockLocalDataClass>()

        val data =
            mapOf<String, Any?>("nested" to mapOf<String, Any?>("createdAt" to timestamp)).normalizeStringMapNullable()

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        // Assert
        assertEquals(expectedInstant, result.nested?.createdAt)
        assertIs<MockLocalDataClass>(result)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun nullNestedObjectIsDecodedToNullableNestedClassCorrectly() {
        @Serializable
        data class MockNestedDataClass(
            @Contextual
            val createdAt: Instant
        )

        @Serializable
        data class MockLocalDataClass(
            @Contextual
            val nested: MockNestedDataClass?
        )

        val serializer = serializer<MockLocalDataClass>()
        val data = mapOf<String, Any?>("nested" to null).normalizeStringMapNullable()

        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(InstantComponentSerializer) }
                ),
            )

        assertEquals(null, result.nested)
    }
}
