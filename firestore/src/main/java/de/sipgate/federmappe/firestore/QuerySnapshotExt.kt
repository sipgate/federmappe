package de.sipgate.federmappe.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import de.sipgate.federmappe.common.DefaultSerializersModule
import de.sipgate.federmappe.common.ErrorHandler
import de.sipgate.federmappe.common.createDecodableTimestamp
import de.sipgate.federmappe.common.toObjectWithSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> QuerySnapshot.toObjects(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: ErrorHandler<T> = { throw it }
): List<T?> = map { documentSnapshot ->
    try {
        documentSnapshot.data.normalizeStringMap()
            .toObjectWithSerializer<T>(customSerializers = customSerializers)
    } catch (ex: Throwable) {
        errorHandler(ex)
    }
}

fun Map<String, Any>.normalizeStringMap(): Map<String, Any> {
    return mapValues {
        when (val value = it.value) {
            is Timestamp -> value.toDecodableTimestamp()
            else -> value
        }
    }
}

fun Timestamp.toDecodableTimestamp() =
    createDecodableTimestamp(seconds, nanoseconds)
