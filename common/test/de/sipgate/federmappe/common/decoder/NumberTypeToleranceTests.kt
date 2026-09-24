package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * A database picks the number type per value: a whole number arrives as Long, a fractional one as
 * Double, and Int and Float are never handed out at all. Every test here feeds a field the type it
 * does not nominally ask for, which is the combination the older number tests never cover.
 */
class NumberTypeToleranceTests {

    @Test
    fun deserializeWholeNumberFieldFromDouble() {
        // Arrange
        @Serializable data class TestClass(val a: Long)

        val data = mapOf<String, Any?>("a" to 1500.5)

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(1500L, result.a)
    }

    @Test
    fun deserializeFloatingPointFieldFromLong() {
        // Arrange
        @Serializable data class TestClass(val a: Double)

        val data = mapOf<String, Any?>("a" to 0L)

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(0.0, result.a)
    }

    @Test
    fun deserializeIntFieldFromDouble() {
        // Arrange
        @Serializable data class TestClass(val a: Int)

        val data = mapOf<String, Any?>("a" to 42.9)

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(42, result.a)
    }

    @Test
    fun deserializeFloatFieldFromLong() {
        // Arrange
        @Serializable data class TestClass(val a: Float)

        val data = mapOf<String, Any?>("a" to 3L)

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(3f, result.a)
    }

    @Test
    fun deserializeShortAndByteFieldsFromDouble() {
        // Arrange
        @Serializable data class TestClass(val a: Short, val b: Byte)

        val data = mapOf<String, Any?>("a" to 7.5, "b" to 3.5)

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(7, result.a)
        assertEquals(3, result.b)
    }

    /**
     * The case that broke in production: a waveform is a list of floats whose silent parts come in
     * as whole numbers, so the list mixes both types.
     */
    @Test
    fun deserializeListOfFloatFromMixedNumberTypes() {
        // Arrange
        @Serializable data class TestClass(val a: List<Float>)

        val data = mapOf<String, Any?>("a" to listOf(0L, 0.8, 1L, 0.25))

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(listOf(0f, 0.8f, 1f, 0.25f), result.a)
    }

    @Test
    fun deserializeListOfIntFromLongValues() {
        // Arrange
        @Serializable data class TestClass(val a: List<Int>)

        val data = mapOf<String, Any?>("a" to listOf(0L, 1L, 2L))

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(listOf(0, 1, 2), result.a)
    }

    @Test
    fun deserializeListOfLongFromMixedNumberTypes() {
        // Arrange
        @Serializable data class TestClass(val a: List<Long>)

        val data = mapOf<String, Any?>("a" to listOf(0L, 1500.5))

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(listOf(0L, 1500L), result.a)
    }

    @Test
    fun deserializeMapOfIntFromLongValues() {
        // Arrange
        @Serializable data class TestClass(val a: Map<String, Int>)

        val data = mapOf<String, Any?>("a" to mapOf("first" to 1L, "second" to 2L))

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(mapOf("first" to 1, "second" to 2), result.a)
    }

    @Test
    fun deserializeMapOfDoubleFromMixedNumberTypes() {
        // Arrange
        @Serializable data class TestClass(val a: Map<String, Double>)

        val data = mapOf<String, Any?>("a" to mapOf("whole" to 2L, "fractional" to 0.5))

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(mapOf("whole" to 2.0, "fractional" to 0.5), result.a)
    }

    @Test
    fun deserializeNullableNumberFieldFromTheOtherNumberType() {
        // Arrange
        @Serializable data class TestClass(val a: Long?)

        val data = mapOf<String, Any?>("a" to 2.75)

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(2L, result.a)
    }

    /**
     * Firebase never hands out an Int, but the decoders also run on plain maps, so a field must
     * take one.
     */
    @Test
    fun deserializeNumberFieldsFromIntValues() {
        // Arrange
        @Serializable
        data class TestClass(val a: Long, val b: Double, val c: Float, val d: Byte)

        val data = mapOf<String, Any?>("a" to 1, "b" to 2, "c" to 3, "d" to 4)

        // Act
        val result = serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(1L, result.a)
        assertEquals(2.0, result.b)
        assertEquals(3f, result.c)
        assertEquals(4, result.d)
    }

    /**
     * A string in a number field is a broken document, not a type the database chose, so it keeps
     * raising the exception the decoders have always raised for a type mismatch.
     */
    @Test
    fun failsOnANumberFieldThatHoldsAString() {
        // Arrange
        @Serializable data class TestClass(val a: Long)

        val data = mapOf<String, Any?>("a" to "1500")

        // Act, Assert
        assertFailsWith<ClassCastException> {
            serializer<TestClass>().deserialize(StringMapToObjectDecoder(data))
        }
    }
}
