package de.sipgate.federmappe

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WholeNumberTests {
    @Test
    fun deserializeBasicDataClassWithWholeNumberFieldSetToZero() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 0L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(0L, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithWholeNumberFieldSetToOne() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 1L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(1L, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithWholeNumberFieldSetToMinusOne() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to -1L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(-1L, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableWholeNumberFieldSetToNull() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long?)

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
        Assertions.assertNull(result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableWholeNumberFieldSetToZero() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 0L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(0L, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableWholeNumberFieldSetToOne() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 1L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(1L, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableWholeNumberFieldSetToMinusOne() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to -1L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(-1L, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeListOfWholeNumber() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Long>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf(-1L, 0L, 1L),
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
        Assertions.assertEquals(listOf(-1L, 0L, 1L), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
