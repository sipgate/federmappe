package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class)
class StringTests {
    @Test
    fun deserializeEmptyList() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to emptyList<Any>(),
            )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertTrue(result.a.isEmpty())
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeListOfString() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf("a", "b", "c"),
            )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(listOf("a", "b", "c"), result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeListOfNullableString() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String?>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf("a", null, "c"),
            )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(listOf("a", null, "c"), result.a)
        assertIs<TestClass>(result)
    }
}
