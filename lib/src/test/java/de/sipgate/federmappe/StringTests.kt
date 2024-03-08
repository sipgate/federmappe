package de.sipgate.federmappe

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringTests {
    @Test
    fun deserializeEmptyList() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to emptyList<Any>(),
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
        Assertions.assertTrue(result.a.isEmpty())
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeListOfString() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf("a", "b", "c"),
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
        Assertions.assertEquals(listOf("a", "b", "c"), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeListOfNullableString() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String?>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf("a", null, "c"),
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
        Assertions.assertEquals(listOf("a", null, "c"), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
