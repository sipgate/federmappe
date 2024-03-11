package de.sipgate.federmappe.firestore

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
        Assertions.assertInstanceOf(TestClass::class.java, result)
        Assertions.assertEquals(SomeValue.A, result.a.first())
        Assertions.assertEquals(SomeValue.B, result.a.last())
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
        Assertions.assertInstanceOf(TestClass::class.java, result)
        Assertions.assertTrue(result.a.isEmpty())
        Assertions.assertEquals("a is missing", result.b)
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
        Assertions.assertInstanceOf(TestClass::class.java, result)
        Assertions.assertNull(result.a)
        Assertions.assertEquals("a is missing", result.b)
    }
}
