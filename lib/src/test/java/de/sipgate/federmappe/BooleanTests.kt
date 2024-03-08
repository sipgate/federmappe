package de.sipgate.federmappe

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BooleanTests {
    @Test
    fun deserializeBasicDataClassWithBooleanFieldSetToTrue() {
        // Arrange
        @Serializable
        data class TestClass(val a: Boolean)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to true)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertTrue(result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithBooleanFieldSetToFalse() {
        // Arrange
        @Serializable
        data class TestClass(val a: Boolean)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to false)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertFalse(result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableBooleanFieldSetToNull() {
        // Arrange
        @Serializable
        data class TestClass(val a: Boolean?)

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
    fun deserializeBasicDataClassWithNullableBooleanFieldSetToTrue() {
        // Arrange
        @Serializable
        data class TestClass(val a: Boolean?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to true)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(true, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeBasicDataClassWithNullableBooleanFieldSetToFalse() {
        // Arrange
        @Serializable
        data class TestClass(val a: Boolean?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to false)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        Assertions.assertEquals(false, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeListOfBoolean() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Boolean>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf(true, false, true),
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
        Assertions.assertEquals(listOf(true, false, true), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeNullableListOfBooleanContainingElements() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Boolean>?)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf(true, false, true),
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
        Assertions.assertEquals(listOf(true, false, true), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeNullableListOfBooleanBeingNull() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Boolean>?)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to null,
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
        Assertions.assertNull(result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeListOfNullableBooleanWithElements() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Boolean?>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf(true, null, false),
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
        Assertions.assertEquals(listOf(true, null, false), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeNullableListOfNullableBooleanWithElements() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Boolean?>?)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf(true, null, false),
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
        Assertions.assertEquals(listOf(true, null, false), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeNullableListOfNullableBooleanBeingNull() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<Boolean?>?)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to null,
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
        Assertions.assertNull(result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeMapOfStringAndBoolean() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, Boolean>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to mapOf("a" to true, "b" to false, "c" to true),
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
        Assertions.assertEquals(mapOf("a" to true, "b" to false, "c" to true), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeMapOfStringAndNullableBoolean() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, Boolean?>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to mapOf("a" to true, "b" to null, "c" to false),
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
        Assertions.assertEquals(mapOf("a" to true, "b" to null, "c" to false), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeMapOfNullableStringAndBoolean() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String?, Boolean>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to mapOf("a" to true, null to false, "c" to true),
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
        Assertions.assertEquals(mapOf("a" to true, null to false, "c" to true), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeNullableMapOfStringAndBooleanWithElements() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, Boolean>?)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to mapOf("a" to true, "b" to false, "c" to true),
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
        Assertions.assertEquals(mapOf("a" to true, "b" to false, "c" to true), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeNullableMapOfStringAndBooleanWithBeingNull() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, Boolean>?)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to null,
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
        Assertions.assertNull(result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
