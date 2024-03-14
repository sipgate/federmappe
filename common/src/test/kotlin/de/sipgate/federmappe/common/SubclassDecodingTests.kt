package de.sipgate.federmappe.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

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
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertInstanceOf(TestClass::class.java, result)
        assertNotNull(result.a)
        assertEquals("Some String", result.a.b)
    }
}
