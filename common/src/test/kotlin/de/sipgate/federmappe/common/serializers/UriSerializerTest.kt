package de.sipgate.federmappe.common.serializers

import de.sipgate.federmappe.firestore.StringMapToObjectDecoder
import de.sipgate.federmappe.firestore.serializers.UriSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URI


class UriSerializerTest {
    private val uriString = "https://sipgate.de:443/some/path?with=multiple&query=args"
    private val uri = URI.create(uriString)

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
        Assertions.assertEquals(uri, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
