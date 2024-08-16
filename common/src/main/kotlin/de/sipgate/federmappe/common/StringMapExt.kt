package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.decoder.StringMapToObjectDecoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@ExperimentalSerializationApi
inline fun <reified T : Any> Map<String, Any>.toObjectWithSerializer(
    serializer: KSerializer<T> = serializer<T>(),
    customSerializers: SerializersModule = DefaultSerializersModule,
    ignoreUnknownProperties: Boolean = true,
): T = serializer.deserialize(
    StringMapToObjectDecoder(
        this,
        ignoreUnknownProperties = ignoreUnknownProperties,
        serializersModule = customSerializers,
    ),
)
