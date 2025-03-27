package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@Ignore("Fails decoding nested structures")
@OptIn(ExperimentalSerializationApi::class)
internal class SealedTypeTests {

    @Serializable
    data class InnerData(val inner: String)

    @Serializable
    sealed interface BaseType {

        @Serializable
        @SerialName("A")
        data class A(val value: String) : BaseType

        @Serializable
        @SerialName("B")
        data class B(val value: Boolean) : BaseType


        @Serializable
        @SerialName("C")
        data class C(val innerValue: InnerData) : BaseType
    }

    @Test
    fun deserializeBasicDataClassWithStringValue() {
        // Arrange
        @Serializable
        data class TestClass(val a: BaseType)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>(
            "a" to mapOf(
                "type" to "A",
                "value" to mapOf("value" to "some string")
            )
        )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClass>(result)
        assertIs<BaseType.A>(result.a)
        assertEquals("some string", result.a.value)
    }

    @Test
    fun deserializeBasicDataClassWithBooleanFieldSetToTrue() {
        // Arrange
        @Serializable
        data class TestClass(val a: BaseType)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>(
            "a" to mapOf(
                "type" to "B",
                "value" to mapOf("value" to true)
            )
        )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClass>(result)
        assertIs<BaseType.B>(result.a)
        assertTrue(result.a.value)
    }

    @Test
    fun deserializeNestedDataClass() {
        // Arrange
        @Serializable
        data class TestClass(val a: BaseType)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>(
            "a" to mapOf(
                "type" to "C",
                "value" to mapOf(
                    "innerValue" to mapOf(
                        "inner" to "it works!"
                    )
                )
            )
        )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertIs<TestClass>(result)
        assertIs<BaseType.C>(result.a)
        val inner = result.a.innerValue
        assertIs<InnerData>(inner)
        assertEquals("it works!", inner.inner)
    }
}
