package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalSerializationApi::class)
class StringMapDecoderTypeSafetyTest {

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsString() {
        @Serializable
        data class TestData(val value: Boolean)

        val data = mapOf("value" to "String")
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsString() {
        @Serializable
        data class TestData(val value: Int)

        val data = mapOf("value" to "String")
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsString() {
        @Serializable
        data class TestData(val value: Long)

        val data = mapOf("value" to "String")
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsString() {
        @Serializable
        data class TestData(val value: Float)

        val data = mapOf("value" to "String")
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsString() {
        @Serializable
        data class TestData(val value: Double)

        val data = mapOf("value" to "String")
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsString() {
        @Serializable
        data class TestData(val value: Char)

        val data = mapOf("value" to "String")
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsString() {
        @Serializable
        data class TestData(val value: Byte)

        val data = mapOf("value" to "String")
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsBoolean() {
        @Serializable
        data class TestData(val value: Int)

        val data = mapOf("value" to true)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsBoolean() {
        @Serializable
        data class TestData(val value: Long)

        val data = mapOf("value" to true)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsBoolean() {
        @Serializable
        data class TestData(val value: Float)

        val data = mapOf("value" to true)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsBoolean() {
        @Serializable
        data class TestData(val value: Double)

        val data = mapOf("value" to true)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsBoolean() {
        @Serializable
        data class TestData(val value: Byte)

        val data = mapOf("value" to true)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsBoolean() {
        @Serializable
        data class TestData(val value: Char)

        val data = mapOf("value" to true)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenStringIsExpectedButInputIsInt() {
        @Serializable
        data class TestData(val value: String)

        val data = mapOf("value" to 0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsInt() {
        @Serializable
        data class TestData(val value: Boolean)

        val data = mapOf("value" to 0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsInt() {
        @Serializable
        data class TestData(val value: Long)

        val data = mapOf("value" to 0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsInt() {
        @Serializable
        data class TestData(val value: Float)

        val data = mapOf("value" to 0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsInt() {
        @Serializable
        data class TestData(val value: Double)

        val data = mapOf("value" to 0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsInt() {
        @Serializable
        data class TestData(val value: Char)

        val data = mapOf("value" to 0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsInt() {
        @Serializable
        data class TestData(val value: Byte)

        val data = mapOf("value" to 0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenStringIsExpectedButInputIsLong() {
        @Serializable
        data class TestData(val value: String)

        val data = mapOf("value" to 0L)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsLong() {
        @Serializable
        data class TestData(val value: Boolean)

        val data = mapOf("value" to 0L)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }


    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsLong() {
        @Serializable
        data class TestData(val value: Double)

        val data = mapOf("value" to 0L)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsLong() {
        @Serializable
        data class TestData(val value: Float)

        val data = mapOf("value" to 0L)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsLong() {
        @Serializable
        data class TestData(val value: Char)

        val data = mapOf("value" to 0L)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsDouble() {
        @Serializable
        data class TestData(val value: Boolean)

        val data = mapOf("value" to 0.0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsDouble() {
        @Serializable
        data class TestData(val value: Long)

        val data = mapOf("value" to 0.0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsDouble() {
        @Serializable
        data class TestData(val value: Int)

        val data = mapOf("value" to 0.0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsDouble() {
        @Serializable
        data class TestData(val value: Char)

        val data = mapOf("value" to 0.0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsDouble() {
        @Serializable
        data class TestData(val value: Byte)

        val data = mapOf("value" to 0.0)
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenStringIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: String)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenBooleanIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: Boolean)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenDoubleIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: Double)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenIntIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: Int)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenFloatIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: Float)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenCharIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: Char)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenByteIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: Byte)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }

    @Test
    fun testClassCastExceptionIsThrownWhenLongIsExpectedButInputIsMap() {
        @Serializable
        data class TestData(val value: Long)

        val data = mapOf<String, Map<String, String>>("value" to emptyMap())
        val serializer = serializer<TestData>()
        assertFailsWith<ClassCastException> {
            serializer.deserialize(StringMapToObjectDecoder(data))
        }
    }
}
