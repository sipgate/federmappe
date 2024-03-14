package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.serializers.DateSerializer
import de.sipgate.federmappe.common.serializers.UriSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val DefaultSerializersModule = SerializersModule {
    contextual(DateSerializer)
    contextual(UriSerializer)
}
