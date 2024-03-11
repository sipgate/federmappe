package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule

class SnapshotDecoder(
    private val dataSnapshot: DataSnapshot,
    private val ignoreUnknownProperties: Boolean,
    override val serializersModule: SerializersModule
) : AbstractDecoder() {

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return CompositeDecoder.DECODE_DONE
    }
}
