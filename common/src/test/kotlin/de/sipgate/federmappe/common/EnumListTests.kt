package de.sipgate.federmappe.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class)
class EnumListTests {

    enum class SomeValue {
        A, B
    }

    @Test
    fun testStringListDecodes() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<SomeValue>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to listOf("A", "B"))

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClass>(result)
        assertEquals(SomeValue.A, result.a.first())
        assertEquals(SomeValue.B, result.a.last())
    }

    @Test
    fun testStringListDecodesDefaultValue() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<SomeValue> = emptyList(), val b: String)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("b" to "a is missing")

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClass>(result)
        assertTrue(result.a.isEmpty())
        assertEquals("a is missing", result.b)
    }

    @Test
    fun testStringListDecodesDefaultNullValue() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<SomeValue>? = null, val b: String)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("b" to "a is missing")

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClass>(result)
        assertNull(result.a)
        assertEquals("a is missing", result.b)
    }
}
