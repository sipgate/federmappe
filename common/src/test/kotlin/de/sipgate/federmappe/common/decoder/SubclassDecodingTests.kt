package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

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
        assertIs<TestClass>(result)
        assertNotNull(result.a)
        assertEquals("Some String", result.a.b)
    }
}
