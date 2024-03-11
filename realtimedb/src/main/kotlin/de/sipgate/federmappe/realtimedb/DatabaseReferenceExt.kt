package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer


@ExperimentalSerializationApi
inline fun <reified T: Any> DatabaseReference.toObject(
    serializer: KSerializer<T> = serializer<T>(),
    crossinline errorHandler: (Throwable) -> T? = { throw it }
): Flow<T> {
    return callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                close(IllegalStateException(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val decodedAppData =
                    try {
                        dataSnapshot.toObjectWithSerializer(serializer)
                    } catch (ex: SerializationException) {
                        errorHandler(ex)
                    }

                if (decodedAppData != null) {
                    trySend(decodedAppData)
                }
            }
        }

        this@toObject.addValueEventListener(valueEventListener)

        awaitClose { this@toObject.removeEventListener(valueEventListener) }
    }
}

