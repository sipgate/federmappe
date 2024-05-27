package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@Serializable
enum class TestEnum {
    A,
    B,
}

@OptIn(ExperimentalSerializationApi::class)
class EnumTests {
    @Test
    fun deserializeDataClassWithEnum() {
        // Arrange
        @Serializable
        data class TestClass(val a: TestEnum)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to TestEnum.A)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(TestEnum.A, result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithNullableEnum() {
        // Arrange
        @Serializable
        data class TestClass(val a: TestEnum?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to TestEnum.A)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(TestEnum.A, result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithNullEnum() {
        // Arrange
        @Serializable
        data class TestClass(val a: TestEnum?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to null)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertNull(result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithInnerEnum() {
        // Arrange
        @Serializable
        data class TestClass2(val a: TestEnum)

        @Serializable
        data class TestClass(val a: TestClass2)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("a" to TestEnum.A))

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(TestEnum.A, result.a.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithMapWithInnerEnum() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, TestEnum>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("a" to TestEnum.A))

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(TestEnum.A, result.a.values.first())
        assertIs<TestClass>(result)
    }
}
