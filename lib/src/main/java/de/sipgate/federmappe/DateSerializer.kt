package de.sipgate.federmappe

import java.util.Date
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
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

    override fun deserialize(decoder: Decoder): Date =
        decoder.decodeStructure(descriptor = descriptor) {
            val seconds = decodeLongElement(descriptor, 0)
            val nanoSeconds = decodeIntElement(descriptor, 1)
            Date(seconds * 1000 + nanoSeconds / 1000000)
        }
}
