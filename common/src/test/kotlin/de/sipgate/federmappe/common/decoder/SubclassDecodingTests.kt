package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalSerializationApi::class)
class SubclassDecodingTests {
    @Serializable
    internal data class A(val b: String)

    @Test
    fun deserializeNestedDataClass() {
        // Arrange
        @Serializable
        data class TestClass(val a: A)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("b" to "Some String"))

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClass>(result)
        assertNotNull(result.a)
        assertEquals("Some String", result.a.b)
    }

    @Test
    fun deserializeNullableNestedDataClassWithValue() {
        @Serializable
        data class TestClass(val a: A?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("b" to "Some String"))

        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        assertIs<TestClass>(result)
        assertEquals("Some String", result.a?.b)
    }

    @Test
    fun deserializeNullableNestedDataClassSetToNull() {
        @Serializable
        data class TestClass(val a: A?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to null)

        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        assertIs<TestClass>(result)
        assertNull(result.a)
    }

    @Test
    fun deserializeDeeplyNestedDataClass() {
        @Serializable
        data class Inner(val c: String)
        @Serializable
        data class Middle(val inner: Inner)
        @Serializable
        data class TestClass(val a: Middle)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("inner" to mapOf("c" to "Some String")))

        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        assertIs<TestClass>(result)
        assertEquals("Some String", result.a.inner.c)
    }
}
