package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import de.sipgate.federmappe.common.ErrorHandler
import de.sipgate.federmappe.common.decoder.DataNormalizer
import de.sipgate.federmappe.common.decoder.DummyDataNormalizer
import de.sipgate.federmappe.common.toObjectWithSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@ExperimentalSerializationApi
inline fun <reified T : Any> DataSnapshot.toObject(
    customSerializers: SerializersModule = EmptySerializersModule(),
    ignoreUnknownProperties: Boolean = false,
    crossinline errorHandler: ErrorHandler<T> = { throw it },
    dataNormalizer: DataNormalizer = DummyDataNormalizer()
): T? = try {
    toObjectMap()
        .unwrapRoot()
        .toObjectWithSerializer(
            serializer = serializer(),
            customSerializers = customSerializers,
            ignoreUnknownProperties = ignoreUnknownProperties,
            dataNormalizer = dataNormalizer
        )
} catch (ex: Throwable) {
    errorHandler(ex)
}

fun DataSnapshot.toObjectMap(): Pair<String, Any> =
    (key ?: "root") to (if (hasChildren()) children.associate { it.toObjectMap() } else value!!)

@Suppress("UNCHECKED_CAST")
fun Pair<String, Any>.unwrapRoot() = second as Map<String, Any>
