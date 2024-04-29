package de.sipgate.federmappe.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringMapDecoderTypeSafetyTest {

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsString() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Boolean, String>("String")
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsString() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Int, String>("String")
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsString() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Long, String>("String")
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsString() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Float, String>("String")
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsString() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Double, String>("String")
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsString() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Char, String>("String")
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsString() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Byte, String>("String")
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsBoolean() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Int, Boolean>(true)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsBoolean() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Long, Boolean>(true)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsBoolean() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Float, Boolean>(true)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsBoolean() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Double, Boolean>(true)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsBoolean() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Byte, Boolean>(true)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsBoolean() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Char, Boolean>(true)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenStringIsExpectedButInputIsInt() {
        expectClassCastExceptionWhenWrongTypeIsGiven<String, Int>(0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsInt() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Boolean, Int>(0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsInt() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Long, Int>(0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsInt() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Float, Int>(0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsInt() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Double, Int>(0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsInt() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Char, Int>(0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsInt() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Byte, Int>(0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenStringIsExpectedButInputIsLong() {
        expectClassCastExceptionWhenWrongTypeIsGiven<String, Long>(0L)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsLong() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Boolean, Long>(0L)
    }


    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsLong() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Double, Long>(0L)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsLong() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Int, Long>(0L)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsLong() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Float, Long>(0L)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsLong() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Char, Long>(0L)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsDouble() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Boolean, Double>(0.0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsDouble() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Long, Double>(0.0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsDouble() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Int, Double>(0.0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsDouble() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Float, Double>(0.0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsDouble() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Char, Double>(0.0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsDouble() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Byte, Double>(0.0)
    }

    @Test
    fun testClassCastExceptionIsThrownWhenStringIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<String, Map<String, String>>(emptyMap())
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Boolean, Map<String, String>>(emptyMap())
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Double, Map<String, String>>(emptyMap())
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Int, Map<String, String>>(emptyMap())
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Float, Map<String, String>>(emptyMap())
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Char, Map<String, String>>(emptyMap())
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Byte, Map<String, String>>(emptyMap())
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsMap() {
        expectClassCastExceptionWhenWrongTypeIsGiven<Long, Map<String, String>>(emptyMap())
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun <E, A> expectClassCastExceptionWhenWrongTypeIsGiven(actual: A) {
        @Serializable
        data class TestData<T>(val value: T)

        val data = mapOf("value" to actual)

        val serializer = serializer<TestData<E>>()

        Assertions.assertThrows(ClassCastException::class.java) {
            serializer.deserialize(
                StringMapToObjectDecoder(data, ignoreUnknownProperties = true)
            )
        }
    }
}
