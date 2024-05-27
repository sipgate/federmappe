package de.sipgate.federmappe.common.decoder

import de.sipgate.federmappe.common.SealedClassWithTypeSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
internal object SealedTypeSerializer : SealedClassWithTypeSerializer<SealedTypeTests.BaseType, String>(
    SealedTypeTests.BaseType::class) {
    override fun selectDeserializer(element: String): DeserializationStrategy<SealedTypeTests.BaseType> {
        return when (element) {
            "A" -> SealedTypeTests.BaseType.A.serializer()
            "B" -> SealedTypeTests.BaseType.B.serializer()
            "C" -> SealedTypeTests.BaseType.C.serializer()
            else -> throw IllegalArgumentException("wah")
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal class SealedTypeTests {

    @Serializable
    data class InnerData(val inner: String)

    @Serializable(with = SealedTypeSerializer::class)
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
                "value" to "some string"
            )
        )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

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
                "value" to true
            )
        )

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

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
                "innerValue" to mapOf(
                    "inner" to "it works!"
                )
            )
        )

        // Act
        val result = serializer.deserialize(
            StringMapToObjectDecoder(
                data,
                ignoreUnknownProperties = true
            )
        )

        // Assert
        assertIs<TestClass>(result)
        assertIs<BaseType.C>(result.a)
        val inner = result.a.innerValue
        assertIs<InnerData>(inner)
        assertEquals("it works!", inner.inner)
    }
}
