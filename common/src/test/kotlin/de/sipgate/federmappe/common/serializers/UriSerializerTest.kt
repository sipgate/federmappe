package de.sipgate.federmappe.common.serializers

import de.sipgate.federmappe.common.StringMapToObjectDecoder
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


class UriSerializerTest {
    private val uriString = "https://sipgate.de:443/some/path?with=multiple&query=args"
    private val uri = URI.create(uriString)

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testDeserialization() {
        @Serializable
        data class TestClass(val a: @Contextual URI)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to uriString)

        val result =
            serializer.deserialize(
                StringMapToObjectDecoder(
                    data,
                    serializersModule = SerializersModule { contextual(UriSerializer) },
                    ignoreUnknownProperties = true,
                ),
            )

        // Assert
        assertEquals(uri, result.a)
        assertIs<TestClass>(result)
    }
}
