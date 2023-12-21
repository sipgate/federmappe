package de.sipgate.federmappe

import com.google.firebase.firestore.DocumentSnapshot

inline fun <reified T : Any> DocumentSnapshot.toObject(errorHandler: (Throwable) -> T? = { throw it }): T? =
    try {
        data?.toObjectWithSerializer<T>()
    } catch (ex: Throwable) {
        errorHandler(ex)
    }
