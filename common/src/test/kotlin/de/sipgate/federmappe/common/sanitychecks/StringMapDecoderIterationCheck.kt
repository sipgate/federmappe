package de.sipgate.federmappe.common.sanitychecks

import de.sipgate.federmappe.common.decoder.StringMapToObjectDecoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.decodeStructure
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class StringMapDecoderIterationCheck {

    @Test
    fun singleItemIsEmitted() {
        @Serializable
        data class Wrapper(val firstKey: String)

        val data = mapOf("firstKey" to "someValue")

        val stringMapDecoder = StringMapToObjectDecoder(data)
        val serializer = Wrapper.serializer()

        val descriptor = serializer.descriptor
        stringMapDecoder.decodeStructure(descriptor) {
            val firstIndex = decodeElementIndex(descriptor)
            assertEquals("firstKey", descriptor.getElementName(firstIndex))
            assertEquals("someValue", decodeStringElement(descriptor, firstIndex))

            val lastIndex = decodeElementIndex(descriptor)
            assertEquals(CompositeDecoder.DECODE_DONE, lastIndex)
        }
    }

    @Test
    fun twoItemsAreEmitted() {
        @Serializable
        data class Wrapper(val firstKey: String)

        val data = mapOf("firstKey" to "someValue", "secondKey" to "someOtherValue")

        val stringMapDecoder = StringMapToObjectDecoder(data)
        val serializer = Wrapper.serializer()

        val descriptor = serializer.descriptor
        stringMapDecoder.decodeStructure(descriptor) {
            val firstIndex = decodeElementIndex(descriptor)
            assertEquals("firstKey", descriptor.getElementName(firstIndex))
            assertEquals("someValue", decodeStringElement(descriptor, firstIndex))

            val secondIndex = decodeElementIndex(descriptor)
            assertEquals("secondKey", descriptor.getElementName(secondIndex))
            assertEquals("someOtherValue", decodeStringElement(descriptor, secondIndex))

            val lastIndex = decodeElementIndex(descriptor)
            assertEquals(CompositeDecoder.DECODE_DONE, lastIndex)
        }
    }
}
