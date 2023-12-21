package de.sipgate.federmappe

import com.google.firebase.firestore.QuerySnapshot

inline fun <reified T : Any> QuerySnapshot.toObject(errorHandler: (Throwable) -> T? = { throw it }): List<T?> =
    map {
        try {
            it.data.toObjectWithSerializer()
        } catch (ex: Throwable) {
            errorHandler(ex)
        }
    }
