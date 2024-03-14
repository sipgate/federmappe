package de.sipgate.federmappe.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import de.sipgate.federmappe.common.DefaultSerializersModule
import de.sipgate.federmappe.common.toObjectWithSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> DocumentSnapshot.toObject(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: (Throwable) -> T? = { throw it }): T? =
    try {
        data?.toObjectWithSerializer<T>(
            customSerializers = customSerializers,
            subtypeDecoder = { (it as? Timestamp)?.let(::FirebaseTimestampDecoder) }
        )
    } catch (ex: Throwable) {
        errorHandler(ex)
    }
