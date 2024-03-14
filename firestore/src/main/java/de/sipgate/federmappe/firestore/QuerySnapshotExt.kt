package de.sipgate.federmappe.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import de.sipgate.federmappe.common.DefaultSerializersModule
import de.sipgate.federmappe.common.toObjectWithSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> QuerySnapshot.toObject(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: (Throwable) -> T? = { throw it }
): List<T?> =
    map { documentSnapshot ->
        try {
            documentSnapshot.data.toObjectWithSerializer(
                customSerializers = customSerializers,
                subtypeDecoder = { (it as? Timestamp)?.let(::FirebaseTimestampDecoder) })
        } catch (ex: Throwable) {
            errorHandler(ex)
        }
    }
