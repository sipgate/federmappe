package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

class PolymorphicDecoder(
    private val type: String,
    private val dataSnapshot: DataSnapshot,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    private val ignoreUnknownProperties: Boolean = false,
) : AbstractDecoder() {

    private var state = DecodeState.Empty
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {

        return when (state) {
            DecodeState.Empty -> {
                state = DecodeState.TypeInfo
                0
            }

            DecodeState.TypeInfo -> {
                state = DecodeState.Data
                1
            }

            DecodeState.Data, DecodeState.None -> {
                state = DecodeState.None
                CompositeDecoder.DECODE_DONE
            }
        }
    }

    override fun decodeString(): String {
        return if (state == DecodeState.TypeInfo) type else super.decodeString()
    }

    override fun decodeValue(): Any {
        return if (state == DecodeState.Data) dataSnapshot else super.decodeValue()
    }
}

enum class DecodeState {
    Empty,
    None,
    TypeInfo,
    Data
}
