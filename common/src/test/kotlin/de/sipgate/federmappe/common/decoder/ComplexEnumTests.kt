package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs

/*
 * This `@Serializable` annotation needs to be present for the `@SerialName` annotation to be
 * processed and attached to the generated SerialDescriptor. This seems to be a side-effect of
 * Kotlin JS deserialization. See https://github.com/Kotlin/kotlinx.serialization/issues/31#issuecomment-1446310243
 * for details
 */
@Serializable
enum class CustomTestEnum {
    @SerialName("avalue") A,
}

private val expectedError = "Couldn't find matching de.sipgate.federmappe.common.decoder.CustomTestEnum enum for value A"

@OptIn(ExperimentalSerializationApi::class)
class ComplexEnumTests {
    @Test
    fun deserializeDataClassWithEnum() {
        // Arrange
        @Serializable
        data class TestClassA(val a: CustomTestEnum)

        val serializer = serializer<TestClassA>()
        val data = mapOf<String, Any?>("a" to CustomTestEnum.A)

        // Act
        val result = assertFails { serializer.deserialize(StringMapToObjectDecoder(data)) }

        // Assert
        assertIs<SerializationException>(result)
        assertEquals(expectedError, result.message)
    }

    @Test
    fun deserializeDataClassWithEnumValue() {
        // Arrange
        @Serializable
        data class TestClassA(val a: CustomTestEnum)

        val serializer = serializer<TestClassA>()
        val data = mapOf<String, Any?>("a" to "A")

        // Act
        val result = assertFails { serializer.deserialize(StringMapToObjectDecoder(data)) }

        // Assert
        assertIs<SerializationException>(result)
        assertEquals(expectedError, result.message)
    }

    @Test
    fun deserializeEnumWithCustomSerialName() {
        // Arrange
        @Serializable
        data class TestClassA(val a: CustomTestEnum)

        val serializer = serializer<TestClassA>()
        val data = mapOf<String, Any?>("a" to "avalue")

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClassA>(result)
        assertEquals(CustomTestEnum.A, result.a)
    }
}
