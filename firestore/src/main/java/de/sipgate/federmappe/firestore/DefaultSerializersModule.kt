package de.sipgate.federmappe.firestore

import de.sipgate.federmappe.firestore.serializers.DateSerializer
import de.sipgate.federmappe.firestore.serializers.UriSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val DefaultSerializersModule = SerializersModule {
    contextual(DateSerializer)
    contextual(UriSerializer)
}
