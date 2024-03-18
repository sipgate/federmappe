package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DatabaseReference
import de.sipgate.federmappe.common.ErrorHandler
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
suspend inline fun <reified T : Any> DatabaseReference.toObject(
    ignoreUnknownProperties: Boolean = false,
    crossinline errorHandler: ErrorHandler<T> = { throw it }
): T? = get().await().toObject<T>(
    errorHandler = errorHandler,
    ignoreUnknownProperties = ignoreUnknownProperties
)

@ExperimentalSerializationApi
suspend inline fun <reified T : Any> DatabaseReference.toObjects(
    ignoreUnknownProperties: Boolean = false,
    crossinline errorHandler: ErrorHandler<T> = { throw it }
): List<T> = get().await().children.mapNotNull { childSnapshot ->
    childSnapshot.toObject<T>(
        errorHandler = errorHandler,
        ignoreUnknownProperties = ignoreUnknownProperties
    )
}
