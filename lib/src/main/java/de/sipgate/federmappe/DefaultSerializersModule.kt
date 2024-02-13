package de.sipgate.federmappe

import de.sipgate.federmappe.serializers.DateSerializer
import de.sipgate.federmappe.serializers.UriSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val DefaultSerializersModule = SerializersModule {
    contextual(DateSerializer)
    contextual(UriSerializer)
}
