package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.SealedTypeTests.BaseType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
internal class SealedTypeTests {

    @Serializable
    sealed interface BaseType {

        @Serializable
        @SerialName("A")
        data class A(val value: String): BaseType

        @Serializable
        @SerialName("B")
        data class B(val value: Boolean) : BaseType
    }

    @Test
    fun deserializeBasicDataClassWithBooleanFieldSetToTrue() {
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
