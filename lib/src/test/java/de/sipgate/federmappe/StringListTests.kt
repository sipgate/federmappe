package de.sipgate.federmappe

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringListTests {

    @Test
    fun testStringListDecodes() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to listOf("A", "B"))

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        Assertions.assertInstanceOf(TestClass::class.java, result)
        Assertions.assertEquals("A", result.a.first())
        Assertions.assertEquals("B", result.a.last())
    }

    @Test
    fun testStringListDecodesDefaultValue() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String> = emptyList(), val b: String)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("b" to "a is missing")

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        Assertions.assertInstanceOf(TestClass::class.java, result)
        Assertions.assertTrue(result.a.isEmpty())
        Assertions.assertEquals("a is missing", result.b)
    }

    @Test
    fun testStringListDecodesDefaultNullValue() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>? = null, val b: String)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("b" to "a is missing")

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        Assertions.assertInstanceOf(TestClass::class.java, result)
        Assertions.assertNull(result.a)
        Assertions.assertEquals("a is missing", result.b)
    }
}
