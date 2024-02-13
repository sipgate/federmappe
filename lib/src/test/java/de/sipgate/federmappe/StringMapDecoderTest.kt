package de.sipgate.federmappe

import com.google.firebase.Timestamp
import de.sipgate.federmappe.serializers.DateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.Date
import java.util.GregorianCalendar
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.reflect.cast

class StringMapDecoderTest {
    @Serializable
    enum class TestEnum {
        A,
        B,
    }

    @Nested
    @DisplayName("Boolean Tests")
    inner class BooleanTests {
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

    @Nested
    @DisplayName("Whole Number Tests")
    inner class WholeNumberTests {
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

    @Nested
    @DisplayName("Floating Point Number Tests")
    inner class FloatingPointNumberTests {
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
    }

    @Nested
    @DisplayName("Date Tests")
    inner class DateTests {
        @Test
        fun deserializeBasicDataClassWithDateFieldSetToFirstDayOfYear2000() {
            // Arrange
            @Serializable
            data class TestClass(
                @Contextual val a: Date,
            )

            val serializer = serializer<TestClass>()
            val data =
                mapOf<String, Any?>(
                    "a" to Timestamp(GregorianCalendar(2000, 0, 1).time),
                )

            // Act
            val result =
                serializer.deserialize(
                    StringMapToObjectDecoder(
                        data,
                        ignoreUnknownProperties = true,
                        serializersModule = SerializersModule { contextual(DateSerializer) },
                    ),
                )

            // Assert
            Assertions.assertEquals(GregorianCalendar(2000, 0, 1).time, result.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
        }

        @Test
        fun deserializeBasicDataClassWithDateFieldAndFixedSerializer() {
            // Arrange
            @Serializable
            data class TestClass(
                @Serializable(with = DateSerializer::class) val a: Date,
            )

            val serializer = serializer<TestClass>()
            val data =
                mapOf<String, Any?>(
                    "a" to Timestamp(GregorianCalendar(2000, 0, 1).time),
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
            Assertions.assertEquals(GregorianCalendar(2000, 0, 1).time, result.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
        }

        @Test
        fun deserializeBasicDataClassWithNullableDateField() {
            // Arrange
            @Serializable
            data class TestClass(
                @Contextual val a: Date?,
            )

            val serializer = serializer<TestClass>()
            val data =
                mapOf<String, Any?>(
                    "a" to Timestamp(GregorianCalendar(2000, 0, 1).time),
                )

            // Act
            val result =
                serializer.deserialize(
                    StringMapToObjectDecoder(
                        data,
                        ignoreUnknownProperties = true,
                        serializersModule = SerializersModule { contextual(DateSerializer) },
                    ),
                )

            // Assert
            Assertions.assertEquals(GregorianCalendar(2000, 0, 1).time, result.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
        }

        @Test
        fun deserializeBasicDataClassWithNullDateField() {
            // Arrange
            @Serializable
            data class TestClass(
                @Contextual val a: Date?,
            )

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
                        serializersModule = SerializersModule { contextual(DateSerializer) },
                    ),
                )

            // Assert
            Assertions.assertNull(result.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
        }

        @Test
        fun deserializeListOfDate() {
            // Arrange
            @Serializable
            data class TestClass(val a: List<@Contextual Date>)

            val serializer = serializer<TestClass>()
            val data =
                mapOf<String, Any?>(
                    "a" to
                        listOf(
                            Timestamp(GregorianCalendar(2000, 0, 1).time),
                            Timestamp(GregorianCalendar(2000, 0, 2).time),
                            Timestamp(GregorianCalendar(2000, 0, 3).time),
                        ),
                )

            // Act
            val result =
                serializer.deserialize(
                    StringMapToObjectDecoder(
                        data,
                        ignoreUnknownProperties = true,
                        serializersModule = SerializersModule { contextual(DateSerializer) },
                    ),
                )

            // Assert
            Assertions.assertEquals(
                listOf(
                    GregorianCalendar(2000, 0, 1).time,
                    GregorianCalendar(2000, 0, 2).time,
                    GregorianCalendar(2000, 0, 3).time,
                ),
                result.a,
            )
            Assertions.assertInstanceOf(TestClass::class.java, result)
        }
    }

    @Nested
    @DisplayName("String Tests")
    inner class StringTests {
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

    @Nested
    @DisplayName("Enum Tests")
    inner class EnumTests {
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
            Assertions.assertEquals(TestEnum.A, result.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
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
            Assertions.assertEquals(TestEnum.A, result.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
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
            Assertions.assertNull(result.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
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
            Assertions.assertEquals(TestEnum.A, result.a.a)
            Assertions.assertInstanceOf(TestClass::class.java, result)
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
            Assertions.assertEquals(TestEnum.A, result.a.values.first())
            Assertions.assertInstanceOf(TestClass::class.java, result)
        }
    }

    @Test
    fun deserializeBasicDataClassSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: String, val b: Int)

        val serializer = serializer<TestClass>()
        val data = mapOf("a" to "test", "b" to 1)

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals("test", result.a)
        Assertions.assertEquals(1, result.b)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeDataClassWithNullSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: String?, val b: Int?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to null, "b" to null)

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertNull(result.a)
        Assertions.assertNull(result.b)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeDataWithExtraFieldsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: String?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to "testString", "b" to 1)

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals("testString", result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeDataClassWithEmptyListOfStringsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to emptyList<String>())

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals(emptyList<String>(), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeDataClassWithFilledListOfStringsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to listOf("testString", "testString2"))

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals(listOf("testString", "testString2"), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeDataClassWithStackedMapsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, Map<String, String>>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("test" to mapOf("test2" to "testString2")))

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals(mapOf("test" to mapOf("test2" to "testString2")), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeDataClassWithFilledMapSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, String>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>("a" to mapOf("test" to "testString", "test2" to "testString2"))

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals(mapOf("test" to "testString", "test2" to "testString2"), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeDataClassWithObject() {
        // Arrange
        @Serializable
        data class TestClass2(val a: String)

        @Serializable
        data class TestClass(val a: TestClass2)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("a" to "testString"))

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals(TestClass2("testString"), result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeComplexStackedDataClass() {
        // Arrange
        @Serializable
        data class TestClass3(val a: String)

        @Serializable
        data class TestClass2(val a: TestClass3)

        @Serializable
        data class TestClass(val a: List<TestClass2>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to listOf(mapOf("a" to mapOf("a" to "some-string"))),
            )

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertEquals("some-string", result.a.first().a.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    @Test
    fun deserializeComplexStackedDataClassAndNullability() {
        // Arrange
        @Serializable
        data class TestClass3(val a: String)

        @Serializable
        data class TestClass2(val a: TestClass3?)

        @Serializable
        data class TestClass(val a: List<TestClass2>)

        val serializer = serializer<TestClass>()
        val data =
            mapOf<String, Any?>(
                "a" to
                    listOf(
                        mapOf("a" to null),
                        mapOf(
                            "a" to mapOf("a" to "some-string"),
                        ),
                    ),
            )

        // Act
        val result =
            serializer.deserialize(StringMapToObjectDecoder(data, ignoreUnknownProperties = true))

        // Assert
        Assertions.assertNull(result.a.first().a?.a)
        Assertions.assertEquals("some-string", result.a[1].a?.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }

    data class ClassCastExceptionInput(
        val valueInMap: Any?,
        val expectedValue: KClass<*>,
    )

    @ParameterizedTest
    @MethodSource("classCastInput")
    fun classCastExceptionIsThrownWhenTheMapContainsTheWrongType(input: ClassCastExceptionInput) {
        Assertions.assertThrows(ClassCastException::class.java) { input.expectedValue.cast(input.valueInMap) }
    }

    companion object {
        private fun classCastExceptionArg(
            valueInMap: Any?,
            expectedValue: KClass<*>,
        ) = Arguments.of(
            ClassCastExceptionInput(
                valueInMap = valueInMap,
                expectedValue = expectedValue,
            ),
        )

        @JvmStatic
        private fun classCastInput(): Stream<Arguments> =
            Stream.of(
                classCastExceptionArg(valueInMap = "string", expectedValue = Boolean::class),
                classCastExceptionArg(valueInMap = "string", expectedValue = Int::class),
                classCastExceptionArg(valueInMap = "string", expectedValue = Double::class),
                classCastExceptionArg(valueInMap = "string", expectedValue = Long::class),
                classCastExceptionArg(valueInMap = "string", expectedValue = Float::class),
                classCastExceptionArg(valueInMap = "string", expectedValue = Char::class),
                classCastExceptionArg(valueInMap = "string", expectedValue = Byte::class),
                classCastExceptionArg(valueInMap = true, expectedValue = String::class),
                classCastExceptionArg(valueInMap = true, expectedValue = Int::class),
                classCastExceptionArg(valueInMap = true, expectedValue = Double::class),
                classCastExceptionArg(valueInMap = true, expectedValue = Long::class),
                classCastExceptionArg(valueInMap = true, expectedValue = Float::class),
                classCastExceptionArg(valueInMap = true, expectedValue = Char::class),
                classCastExceptionArg(valueInMap = true, expectedValue = Byte::class),
                classCastExceptionArg(valueInMap = 0, expectedValue = String::class),
                classCastExceptionArg(valueInMap = 0, expectedValue = Boolean::class),
                classCastExceptionArg(valueInMap = 0, expectedValue = Double::class),
                classCastExceptionArg(valueInMap = 0, expectedValue = Long::class),
                classCastExceptionArg(valueInMap = 0, expectedValue = Float::class),
                classCastExceptionArg(valueInMap = 0, expectedValue = Char::class),
                classCastExceptionArg(valueInMap = 0, expectedValue = Byte::class),
                classCastExceptionArg(valueInMap = 0L, expectedValue = String::class),
                classCastExceptionArg(valueInMap = 0L, expectedValue = Boolean::class),
                classCastExceptionArg(valueInMap = 0L, expectedValue = Double::class),
                classCastExceptionArg(valueInMap = 0L, expectedValue = Int::class),
                classCastExceptionArg(valueInMap = 0L, expectedValue = Float::class),
                classCastExceptionArg(valueInMap = 0L, expectedValue = Char::class),
                classCastExceptionArg(valueInMap = 0L, expectedValue = Byte::class),
                classCastExceptionArg(valueInMap = 0.4, expectedValue = String::class),
                classCastExceptionArg(valueInMap = 0.4, expectedValue = Boolean::class),
                classCastExceptionArg(valueInMap = 0.4, expectedValue = Long::class),
                classCastExceptionArg(valueInMap = 0.4, expectedValue = Int::class),
                classCastExceptionArg(valueInMap = 0.4, expectedValue = Float::class),
                classCastExceptionArg(valueInMap = 0.4, expectedValue = Char::class),
                classCastExceptionArg(valueInMap = 0.4, expectedValue = Byte::class),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = String::class,
                ),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = Boolean::class,
                ),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = Double::class,
                ),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = Int::class,
                ),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = Float::class,
                ),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = Char::class,
                ),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = Byte::class,
                ),
                classCastExceptionArg(
                    valueInMap = mapOf<String, String>(),
                    expectedValue = Long::class,
                ),
            )
    }
}
