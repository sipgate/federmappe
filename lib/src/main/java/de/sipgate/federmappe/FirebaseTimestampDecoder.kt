package de.sipgate.federmappe

import com.google.firebase.Timestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class FirebaseTimestampDecoder(
    private val timestamp: Timestamp,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
) : AbstractDecoder() {
    internal enum class DecodeState {
        DECODE_SECONDS,
        DECODE_NANOS,
        END_OF_STRUCTURE,
        ;

        fun advance() =
            when (this) {
                DECODE_SECONDS -> DECODE_NANOS
                DECODE_NANOS -> END_OF_STRUCTURE
                else -> END_OF_STRUCTURE
            }
    }

    private var decodeState = DecodeState.DECODE_SECONDS

    override fun decodeSequentially(): Boolean = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = 2

    override fun decodeValue(): Any =
        when (decodeState) {
            DecodeState.DECODE_SECONDS -> timestamp.seconds
            DecodeState.DECODE_NANOS -> timestamp.nanoseconds
            else -> throw SerializationException("Serializer read after end of structure!")
        }.also { decodeState = decodeState.advance() }

    // This method will most likely never be called because decodeSequentially returns true
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        when (decodeState) {
            DecodeState.DECODE_SECONDS -> 0
            DecodeState.DECODE_NANOS -> 1
            DecodeState.END_OF_STRUCTURE -> CompositeDecoder.DECODE_DONE
        }
}
