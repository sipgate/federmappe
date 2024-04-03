package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.SealedTypeTests.BaseType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
internal object SealedTypeSerializer : SealedClassWithTypeSerializer<BaseType, String>(BaseType::class) {
    override fun selectDeserializer(element: String): DeserializationStrategy<BaseType> {
        return when (element) {
            "A" -> BaseType.A.serializer()
            "B" -> BaseType.B.serializer()
            "C" -> BaseType.C.serializer()
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
        assertInstanceOf(TestClass::class.java, result)
        assertInstanceOf(BaseType.A::class.java, result.a)
        assertEquals("some string", (result.a as BaseType.A).value)
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
        assertInstanceOf(TestClass::class.java, result)
        assertInstanceOf(BaseType.B::class.java, result.a)
        assertTrue((result.a as BaseType.B).value)
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
        assertInstanceOf(TestClass::class.java, result)
        assertInstanceOf(BaseType.C::class.java, result.a)
        val inner = (result.a as BaseType.C).innerValue
        assertInstanceOf(InnerData::class.java, inner)
        assertEquals("it works!", inner.inner)
    }
}
