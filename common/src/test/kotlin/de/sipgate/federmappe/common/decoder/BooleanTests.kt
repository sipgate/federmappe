package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class)
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
        assertTrue(result.a)
        assertIs<TestClass>(result)
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
        assertFalse(result.a)
        assertIs<TestClass>(result)
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
        assertNull(result.a)
        assertIs<TestClass>(result)
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
        assertEquals(true, result.a)
        assertIs<TestClass>(result)
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
        assertEquals(false, result.a)
        assertIs<TestClass>(result)
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
        assertEquals(listOf(true, false, true), result.a)
        assertIs<TestClass>(result)
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
        assertEquals(listOf(true, false, true), result.a)
        assertIs<TestClass>(result)
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
        assertNull(result.a)
        assertIs<TestClass>(result)
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
        assertEquals(listOf(true, null, false), result.a)
        assertIs<TestClass>(result)
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
        assertEquals(listOf(true, null, false), result.a)
        assertIs<TestClass>(result)
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
        assertNull(result.a)
        assertIs<TestClass>(result)
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
        assertEquals(mapOf("a" to true, "b" to false, "c" to true), result.a)
        assertIs<TestClass>(result)
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
        assertEquals(mapOf("a" to true, "b" to null, "c" to false), result.a)
        assertIs<TestClass>(result)
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
        assertEquals(mapOf("a" to true, null to false, "c" to true), result.a)
        assertIs<TestClass>(result)
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
        assertEquals(mapOf("a" to true, "b" to false, "c" to true), result.a)
        assertIs<TestClass>(result)
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
        assertNull(result.a)
        assertIs<TestClass>(result)
    }
}
