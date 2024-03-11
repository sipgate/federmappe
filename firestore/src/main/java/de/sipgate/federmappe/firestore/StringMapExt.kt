package de.sipgate.federmappe.firestore

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

inline fun <reified T : Any> Map<String, Any>.toObjectWithSerializer(
    serializer: KSerializer<T> = serializer<T>(),
    customSerializers: SerializersModule
): T = serializer.deserialize(
    StringMapToObjectDecoder(
        this,
        ignoreUnknownProperties = true,
        serializersModule = customSerializers,
    ),
)
