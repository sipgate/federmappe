package de.sipgate.federmappe.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalSerializationApi::class)
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
        assertEquals(0L, result.a)
        assertIs<TestClass>(result)
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
        assertEquals(1L, result.a)
        assertIs<TestClass>(result)
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
        assertEquals(-1L, result.a)
        assertIs<TestClass>(result)
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
        assertNull(result.a)
        assertIs<TestClass>(result)
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
        assertEquals(0L, result.a)
        assertIs<TestClass>(result)
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
        assertEquals(1L, result.a)
        assertIs<TestClass>(result)
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
        assertEquals(-1L, result.a)
        assertIs<TestClass>(result)
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
        assertEquals(listOf(-1L, 0L, 1L), result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun testDownCastingToInt() {
        // Arrange
        @Serializable
        data class TestClass(val a: Int)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 45767L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(45767, result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun testDownCastingToShort() {
        // Arrange
        @Serializable
        data class TestClass(val a: Short)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 3072L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(3072, result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun testDownCastingToByte() {
        // Arrange
        @Serializable
        data class TestClass(val a: Byte)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to 99L)

        // Act
        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(99, result.a)
        assertIs<TestClass>(result)
    }
}
