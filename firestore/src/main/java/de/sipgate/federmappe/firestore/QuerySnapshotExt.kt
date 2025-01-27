package de.sipgate.federmappe.firestore

import com.google.firebase.firestore.QuerySnapshot
import de.sipgate.federmappe.common.DefaultSerializersModule
import de.sipgate.federmappe.common.ErrorHandler
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> QuerySnapshot.toObjects(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: ErrorHandler<T> = { throw it },
): List<T?> = map { documentSnapshot ->
    try {
        documentSnapshot.toObject(customSerializers, errorHandler)
    } catch (ex: Throwable) {
        errorHandler(ex)
    }
}
