package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs

@OptIn(ExperimentalSerializationApi::class)
class StringMapDecoderOptionalsTests {

    @Serializable
    data class TwoNonOptionalFields(
        val first: String,
        val second: String,
    )

    @Test
    fun testTwoArgsWorksForTwoArgStruct() {
        // Arrange
        val serializer = serializer<TwoNonOptionalFields>()
        val data = mapOf<String, Any?>(
            "first" to "a",
            "second" to "b",
        )

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = false,
                )
            )

        // Assert
        assertIs<TwoNonOptionalFields>(result)
        assertEquals("a", result.first)
        assertEquals("b", result.second)
    }

    @Test
    fun failsWhenUnexpectedPropertiesArePresent() {
        // Arrange
        val serializer = serializer<TwoNonOptionalFields>()
        val data = mapOf<String, Any?>(
            "first" to "a",
            "second" to "b",
            "third" to "c",
        )

        // Act
        val result = assertFails {
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = false,
                )
            )
        }

        // Assert
        assertIs<SerializationException>(result)
        assertEquals("found unhandled properties: [third]", result.message)
    }

    @Test
    fun testDoesNotThrowWhenRequestedToIgnoreUnknownProperties() {
        // Arrange
        val serializer = serializer<TwoNonOptionalFields>()
        val data = mapOf<String, Any?>(
            "first" to "a",
            "second" to "b",
            "third" to "c",
        )

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                )
            )

        // Assert
        assertIs<TwoNonOptionalFields>(result)
        assertEquals("a", result.first)
        assertEquals("b", result.second)
    }

    @Test
    fun failsWhenRequiredFieldIsMissing() {
        val serializer = serializer<TwoNonOptionalFields>()
        val data = mapOf<String, Any?>("first" to "a")

        val result = assertFails {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }

        assertIs<SerializationException>(result)
    }
}
