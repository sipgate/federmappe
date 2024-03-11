package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

inline fun <reified T : Any> DataSnapshot.toObject(
    customSerializers: SerializersModule = EmptySerializersModule(),
    crossinline errorHandler: (Throwable) -> T? = { throw it }
): List<T?> = buildList {
    try {
        add(toObjectWithSerializer(customSerializers = customSerializers))
    } catch (ex: Throwable) {
        errorHandler(ex)
    }
}

inline fun <reified T : Any> DataSnapshot.toObjectWithSerializer(
    serializer: KSerializer<T> = serializer<T>(),
    customSerializers: SerializersModule = EmptySerializersModule()
): T = serializer.deserialize(
    SnapshotDecoder(
        this,
        ignoreUnknownProperties = true,
        serializersModule = customSerializers,
    ),
)
