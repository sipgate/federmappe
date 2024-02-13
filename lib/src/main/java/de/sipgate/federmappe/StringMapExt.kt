package de.sipgate.federmappe

import de.sipgate.federmappe.serializers.DateSerializer
import de.sipgate.federmappe.serializers.UriSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer

inline fun <reified T : Any> Map<String, Any>.toObjectWithSerializer(
    serializer: KSerializer<T> = serializer<T>(),
    customSerializers: SerializersModule = SerializersModule {
        contextual(DateSerializer)
        contextual(UriSerializer)
    }
): T = serializer.deserialize(
    StringMapToObjectDecoder(
        this,
        ignoreUnknownProperties = true,
        serializersModule = customSerializers,
    ),
)
