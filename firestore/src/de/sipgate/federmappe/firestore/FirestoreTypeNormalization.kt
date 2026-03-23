package de.sipgate.federmappe.firestore

import com.google.firebase.Timestamp
import de.sipgate.federmappe.firestore.types.toDecodableTimestamp

@Suppress("UNCHECKED_CAST")
fun Map<String, Any>.normalizeStringMap(): Map<String, Any> = mapValues {
    when (val value = it.value) {
        is Timestamp -> value.toDecodableTimestamp()
        is Map<*, *> -> (value as? Map<String, Any>)?.normalizeStringMap() ?: value
        else -> value
    }
}

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.normalizeStringMapNullable(): Map<String, Any?> = mapValues {
    when (val value = it.value) {
        is Timestamp -> value.toDecodableTimestamp()
        is Map<*, *> -> (value as? Map<String, Any?>)?.normalizeStringMapNullable() ?: value
        else -> value
    }
}
