package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.serializers.TimestampToDateSerializer
import de.sipgate.federmappe.common.serializers.UriSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val DefaultSerializersModule = SerializersModule {
    contextual(_root_ide_package_.de.sipgate.federmappe.common.serializers.TimestampToDateSerializer)
    contextual(_root_ide_package_.de.sipgate.federmappe.common.serializers.UriSerializer)
}
