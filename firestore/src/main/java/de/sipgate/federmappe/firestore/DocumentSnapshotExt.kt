package de.sipgate.federmappe.firestore

import com.google.firebase.firestore.DocumentSnapshot
import de.sipgate.federmappe.common.DefaultSerializersModule
import de.sipgate.federmappe.common.ErrorHandler
import de.sipgate.federmappe.common.toObjectWithSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
inline fun <reified T : Any> DocumentSnapshot.toObject(
    customSerializers: SerializersModule = DefaultSerializersModule,
    errorHandler: ErrorHandler<T> = { throw it },
): T? = try {
    data?.normalizeStringMap()?.toObjectWithSerializer<T>(
        customSerializers = customSerializers,
    )
} catch (ex: Throwable) {
    errorHandler(ex)
}
