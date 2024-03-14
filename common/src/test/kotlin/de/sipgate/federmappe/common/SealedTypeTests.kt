package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.SealedTypeTests.BaseType
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
internal class CustomSerializer : SealedClassWithTypeSerializer<BaseType, String>(BaseType::class) {
    override fun selectDeserializer(element: String): DeserializationStrategy<BaseType> {
        return when (element) {
            "A" -> BaseType.A.serializer()
            "B" -> BaseType.B.serializer()
            else -> throw IllegalArgumentException("unknown element $element")
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal class SealedTypeTests {

    @Serializable(with = CustomSerializer::class)
    sealed interface BaseType {
        val type: String

        @Serializable
        data class A(val value: String) : BaseType {
            override val type = "A"
        }

        @Serializable
        data class B(val value: Boolean) : BaseType {
            override val type = "B"
        }
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
                    serializersModule = SerializersModule {
                        polymorphic(BaseType::class) {
                            subclass(BaseType.A::class)
                            subclass(BaseType.B::class)
                        }
                    }
                ),
            )

        // Assert
        assertInstanceOf(TestClass::class.java, result)
        assertInstanceOf(BaseType.A::class.java, result.a)
        assertEquals("some string", (result.a as BaseType.A).value)
    }
}
