package de.sipgate.federmappe.firestore

import com.google.firebase.firestore.QuerySnapshot
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> QuerySnapshot.toObject(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: (Throwable) -> T? = { throw it }): List<T?> =
    map {
        try {
            it.data.toObjectWithSerializer(customSerializers = customSerializers)
        } catch (ex: Throwable) {
            errorHandler(ex)
        }
    }
