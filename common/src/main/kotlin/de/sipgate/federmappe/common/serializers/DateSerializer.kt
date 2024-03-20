package de.sipgate.federmappe.common.serializers

import java.util.Date
import kotlin.properties.Delegates
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object DateSerializer : KSerializer<Date> {
    override val descriptor = buildClassSerialDescriptor(serialName = "FirebaseTimestamp") {
        element<Long>("seconds")
        element<Int>("nanoseconds")
    }

    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeStructure(descriptor) {
        encodeLongElement(descriptor, 0, value.time / 1000)
        encodeIntElement(descriptor, 1, 0)
    }

    @ExperimentalSerializationApi
    override fun deserialize(decoder: Decoder): Date {
        return decoder.decodeStructure(descriptor = descriptor) {
            if (decodeSequentially()) {
                val seconds = decodeLongElement(descriptor, 0)
                val nanoSeconds = decodeIntElement(descriptor, 1)
                Date(seconds * 1000 + nanoSeconds / 1000000)
            } else {
                var seconds by Delegates.notNull<Long>()
                var nanoSeconds by Delegates.notNull<Int>()

                while (true) {
                    when (val index = decodeElementIndex(descriptor)) {
                        0 -> seconds = decodeLongElement(descriptor, 0)
                        1 -> nanoSeconds = decodeIntElement(descriptor, 1)
                        CompositeDecoder.DECODE_DONE -> break
                        else -> error("Unexpected index: $index")
                    }
                }

                Date(seconds * 1000 + nanoSeconds / 1000000)
            }
        }
    }
}
