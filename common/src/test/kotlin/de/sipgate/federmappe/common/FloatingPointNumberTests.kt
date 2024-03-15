package de.sipgate.federmappe.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class FloatingPointNumberTests {
    @Test
    fun deserializeBasicDataClassWithFloatingPointNumberFieldSetToZero() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 0.0)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(0.0, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithFloatingPointNumberFieldSetToOnePointFive() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 1.5)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(1.5, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithFloatingPointNumberFieldSetToMinusOnePointFive() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to -1.5)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(-1.5, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithFloatingPointNumberFieldSetToNaN() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to Double.NaN)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(Double.NaN, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableFloatingPointNumberFieldSetToNull() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double?)

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
    fun deserializeBasicDataClassWithNullableFloatingPointNumberFieldSetToZero() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 0.0)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(0.0, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableFloatingPointNumberFieldSetToOnePointFive() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 1.5)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(1.5, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableFloatingPointNumberFieldSetToMinusOnePointFive() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to -1.5)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(-1.5, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableFloatingPointNumberFieldSetToNaN() {
        // Arrange
        @Serializable
        data class TestClass(val a: Double?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to Double.NaN)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(Double.NaN, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeListOfFloatingPointNumber() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Double>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf(-1.5, 0.0, 1.5),
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
        Assertions.assertEquals(listOf(-1.5, 0.0, 1.5), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun testDownCastingToFloat() {
        // Arrange
        @Serializable
        data class TestClass(val a: Float)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 0.8)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(0.8f, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
