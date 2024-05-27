package de.sipgate.federmappe.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import de.sipgate.federmappe.common.DefaultSerializersModule
import de.sipgate.federmappe.common.ErrorHandler
import de.sipgate.federmappe.common.toObjectWithSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> QuerySnapshot.toObjects(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: ErrorHandler<T> = { throw it }
): List<T?> = map { documentSnapshot ->
    try {
        val a: Map<String, Any> = documentSnapshot.data
        val b: Map<String, Any> = a.normalizeStringMap()
        val c = b.toObjectWithSerializer<T>(customSerializers = customSerializers)
        c
    } catch (ex: Throwable) {
        errorHandler(ex)
    }
}

fun Map<String, Any>.normalizeStringMap(): Map<String, Any> {
    return mapValues {
        when (val value = it.value) {
            is Timestamp -> value.decodeFirestoreTimestamp()
            else -> value
        }
    }
}

fun Timestamp.decodeFirestoreTimestamp() = mapOf(
    "epochSeconds" to seconds,
    "nanosecondsOfSecond" to nanoseconds.toLong(),
)
