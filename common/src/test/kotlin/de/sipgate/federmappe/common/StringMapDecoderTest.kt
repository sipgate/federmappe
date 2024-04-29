package de.sipgate.federmappe.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class StringMapDecoderTest {

    @Test
    fun deserializeBasicDataClassSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: String, val b: Int)

        val serializer = serializer<TestClass>()
        val data = mapOf("a" to "test", "b" to 1L)

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
}
