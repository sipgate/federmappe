package de.sipgate.federmappe.firestore

import com.google.firebase.Timestamp
import de.sipgate.federmappe.common.StringMapToObjectDecoder
import de.sipgate.federmappe.common.serializers.DateSerializer
import java.util.Date
import java.util.GregorianCalendar
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class FirebaseTimestampTests {
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
                    subtypeDecoder = { (it as? Timestamp)?.let(::FirebaseTimestampDecoder) }
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
                    subtypeDecoder = { (it as? Timestamp)?.let(::FirebaseTimestampDecoder) }
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
                    subtypeDecoder = { (it as? Timestamp)?.let(::FirebaseTimestampDecoder) }
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
                    subtypeDecoder = { (it as? Timestamp)?.let(::FirebaseTimestampDecoder) }
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
