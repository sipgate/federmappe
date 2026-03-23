package de.sipgate.federmappe.common.serializers

import java.net.URI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UriSerializer : KSerializer<URI> {
    @InternalSerializationApi
    @ExperimentalSerializationApi
    override val descriptor = buildSerialDescriptor(serialName = "java.net.URI", SerialKind.CONTEXTUAL)

    override fun serialize(encoder: Encoder, value: URI) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): URI = URI.create(decoder.decodeString())
}
