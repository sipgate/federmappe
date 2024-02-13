package de.sipgate.federmappe.serializers

import kotlinx.datetime.Instant
import java.util.Date
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object InstantSerializer : KSerializer<Instant> {
    override val descriptor = buildClassSerialDescriptor(serialName = "FirebaseTimestamp") {
        element<Long>("seconds")
        element<Int>("nanoseconds")
    }

    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeStructure(descriptor) {
        encodeLongElement(descriptor, 0, value.epochSeconds)
        encodeIntElement(descriptor, 1, value.nanosecondsOfSecond)
    }

    override fun deserialize(decoder: Decoder): Instant =
        decoder.decodeStructure(descriptor = descriptor) {
            val seconds = decodeLongElement(descriptor, 0)
            val nanoSeconds = decodeIntElement(descriptor, 1)
            Instant.fromEpochSeconds(seconds, nanoSeconds)
        }
}
