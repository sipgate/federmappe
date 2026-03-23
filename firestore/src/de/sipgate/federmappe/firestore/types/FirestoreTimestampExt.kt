package de.sipgate.federmappe.firestore.types

import com.google.firebase.Timestamp
import de.sipgate.federmappe.common.createDecodableTimestamp

fun Timestamp.toDecodableTimestamp() =
    createDecodableTimestamp(seconds, nanoseconds)
