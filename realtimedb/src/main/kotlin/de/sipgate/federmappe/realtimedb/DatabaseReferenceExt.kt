package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
suspend inline fun <reified T : Any> DatabaseReference.toObject(
    crossinline errorHandler: (Throwable) -> T? = { throw it }
): T? = get().await().toObject<T>(errorHandler = errorHandler)

@ExperimentalSerializationApi
suspend inline fun <reified T : Any> DatabaseReference.toObjects(
    crossinline errorHandler: (Throwable) -> T? = { throw it }
): List<T> = get().await().children.mapNotNull { childSnapshot ->
    childSnapshot.toObject<T>(errorHandler = errorHandler)
}
