package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
inline fun <reified T : Any> DatabaseReference.toObject(
    crossinline errorHandler: (Throwable) -> T? = { throw it }
): Flow<T?> = callbackFlow {
    val valueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            close(IllegalStateException(error.toException()))
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            trySend(dataSnapshot.toObject<T>(errorHandler = errorHandler))
        }
    }

    this@toObject.addValueEventListener(valueEventListener)

    awaitClose { this@toObject.removeEventListener(valueEventListener) }
}

@ExperimentalSerializationApi
inline fun <reified T : Any> DatabaseReference.toObjects(
    crossinline errorHandler: (Throwable) -> T? = { throw it }
): Flow<List<T>> = callbackFlow {
    val valueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            close(IllegalStateException(error.toException()))
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            trySend(dataSnapshot.children.mapNotNull { childSnapshot ->
                childSnapshot.toObject<T>(errorHandler = errorHandler)
            })
        }
    }

    this@toObjects.addValueEventListener(valueEventListener)

    awaitClose { this@toObjects.removeEventListener(valueEventListener) }
}
