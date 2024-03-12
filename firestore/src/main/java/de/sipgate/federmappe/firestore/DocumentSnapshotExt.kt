package de.sipgate.federmappe.firestore

import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> DocumentSnapshot.toObject(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: (Throwable) -> T? = { throw it }): T? =
    try {
        data?.toObjectWithSerializer<T>(customSerializers = customSerializers)
    } catch (ex: Throwable) {
        errorHandler(ex)
    }
