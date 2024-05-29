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
    fun singleAttributeIsDecoded() {
        @Serializable
        data class Wrapper(val firstKey: String, val asdf: String? = null)

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
    fun twoAttributesAreDecoded() {
        @Serializable
        data class Wrapper(val firstKey: String, val asdf: String? = null)

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

    @Test
    fun multipleAttributesAreCompletelyDecoded() {
        @Serializable
        data class Wrapper(val firstKey: String, val asdf: String? = null)

        val data = mapOf(
            "firstKey" to "first",
            "secondKey" to "second",
            "thirdKey" to "third",
            "fourthKey" to "fourth"
        )

        val stringMapDecoder = StringMapToObjectDecoder(data)
        val serializer = Wrapper.serializer()

        val descriptor = serializer.descriptor
        stringMapDecoder.decodeStructure(descriptor) {
            val firstIndex = decodeElementIndex(descriptor)
            assertEquals("firstKey", descriptor.getElementName(firstIndex))
            assertEquals("first", decodeStringElement(descriptor, firstIndex))

            val secondIndex = decodeElementIndex(descriptor)
            assertEquals("secondKey", descriptor.getElementName(secondIndex))
            assertEquals("second", decodeStringElement(descriptor, secondIndex))

            val thirdIndex = decodeElementIndex(descriptor)
            assertEquals("thirdKey", descriptor.getElementName(thirdIndex))
            assertEquals("third", decodeStringElement(descriptor, thirdIndex))

            val fourthIndex = decodeElementIndex(descriptor)
            assertEquals("fourthKey", descriptor.getElementName(fourthIndex))
            assertEquals("fourth", decodeStringElement(descriptor, fourthIndex))

            val lastIndex = decodeElementIndex(descriptor)
            assertEquals(CompositeDecoder.DECODE_DONE, lastIndex)
        }
    }
}
