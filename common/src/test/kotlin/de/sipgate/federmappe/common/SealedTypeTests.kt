package de.sipgate.federmappe.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class SealedTypeTests {

    enum class TypeDecl {
        @SerialName("A_TYPE") A,
        @SerialName("B_TYPE") B
    }

    @Serializable
    sealed interface BaseType {
        val type: TypeDecl

        @Serializable
        data class A(val value: String): BaseType {
            override val type = TypeDecl.A
        }

        @Serializable
        data class B(val value: Boolean): BaseType {
            override val type = TypeDecl.B
        }
    }

    @Test
    fun deserializeBasicDataClassWithBooleanFieldSetToTrue() {
        // Arrange
        @Serializable
        data class TestClass(val a: BaseType)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf(
            "type" to "A_TYPE",
            "value" to "some string"
        ))

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
        assertInstanceOf(BaseType.A::class.java, result.a)
        assertEquals("some string", (result.a as BaseType.A).value)
    }
}
