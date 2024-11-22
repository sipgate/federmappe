package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

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
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals("test", result.a)
        assertEquals(1, result.b)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithNullSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: String?, val b: Int?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to null, "b" to null)

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertNull(result.a)
        assertNull(result.b)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataWithExtraFieldsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: String?)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to "testString", "b" to 1)

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals("testString", result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithEmptyListOfStringsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to emptyList<String>())

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(emptyList<String>(), result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithFilledListOfStringsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: List<String>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to listOf("testString", "testString2"))

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(listOf("testString", "testString2"), result.a)
        assertIs<TestClass>(result)
    }

    @Test
    fun deserializeDataClassWithStackedMapsSucceeds() {
        // Arrange
        @Serializable
        data class TestClass(val a: Map<String, Map<String, String>>)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to mapOf("test" to mapOf("test2" to "testString2")))

        // Act
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(mapOf("test" to mapOf("test2" to "testString2")), result.a)
        assertIs<TestClass>(result)
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
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(mapOf("test" to "testString", "test2" to "testString2"), result.a)
        assertIs<TestClass>(result)
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
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals(TestClass2("testString"), result.a)
        assertIs<TestClass>(result)
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
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertEquals("some-string", result.a.first().a.a)
        assertIs<TestClass>(result)
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
        val result = serializer.deserialize(StringMapToObjectDecoder(data))

        // Assert
        assertNull(result.a.first().a?.a)
        assertEquals("some-string", result.a[1].a?.a)
        assertIs<TestClass>(result)
    }
}
