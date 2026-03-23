package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.decoder.StringMapToObjectDecoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

typealias StringMap = Map<String, Any?>

@ExperimentalSerializationApi
inline fun <reified T : Any> de.sipgate.federmappe.common.StringMap.toObjectWithSerializer(
    serializer: KSerializer<T> = serializer<T>(),
    customSerializers: SerializersModule = _root_ide_package_.de.sipgate.federmappe.common.DefaultSerializersModule,
    ignoreUnknownProperties: Boolean = true,
): T = serializer.deserialize(
    _root_ide_package_.de.sipgate.federmappe.common.decoder.StringMapToObjectDecoder(
        this,
        ignoreUnknownProperties = ignoreUnknownProperties,
        serializersModule = customSerializers,
    ),
)
