package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import de.sipgate.federmappe.common.ErrorHandler
import de.sipgate.federmappe.common.StringMapToObjectDecoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@ExperimentalSerializationApi
inline fun <reified T : Any> DataSnapshot.toObject(
    customSerializers: SerializersModule = EmptySerializersModule(),
    ignoreUnknownProperties: Boolean = false,
    crossinline errorHandler: ErrorHandler<T> = { throw it }
): T? = try {
    toObjectWithSerializer(
        ignoreUnknownProperties = ignoreUnknownProperties,
        customSerializers = customSerializers
    )
} catch (ex: Throwable) {
    errorHandler(ex)
}

@ExperimentalSerializationApi
inline fun <reified T : Any> DataSnapshot.toObjectWithSerializer(
    serializer: KSerializer<T> = serializer<T>(),
    ignoreUnknownProperties: Boolean = false,
    customSerializers: SerializersModule = EmptySerializersModule()
): T = serializer.deserialize(
    StringMapToObjectDecoder(
        data = toObjectMap().unwrapRoot(),
        serializersModule = customSerializers,
        ignoreUnknownProperties = ignoreUnknownProperties
    )
)

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
fun DataSnapshot.toObjectMap(): Pair<String, Any> =
    (key ?: "root") to (if (hasChildren()) children.associate { it.toObjectMap() } else value)!!

@Suppress("UNCHECKED_CAST")
fun Pair<String, Any>.unwrapRoot() = second as Map<String, Any>
