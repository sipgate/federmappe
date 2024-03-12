package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class SnapshotDecoder(
    private val dataSnapshot: DataSnapshot,
    private val ignoreUnknownProperties: Boolean,
    override val serializersModule: SerializersModule
) : AbstractDecoder() {

    private val keysIterator = dataSnapshot.children.mapNotNull { it.key }.iterator()
    private var index: Int? = null
    private var key: String? = null

    private val skippedValues = mutableSetOf<String>()

    override fun decodeSequentially() = false

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = dataSnapshot.children.count()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (keysIterator.hasNext()) {
            val nextKey = keysIterator.next().also { key = it }

            val nextIndex =
                when (descriptor.kind) {
//                    StructureKind.MAP -> dataSnapshot.children.indexOf(nextKey)
                    else -> descriptor.getElementIndex(nextKey)
                }

            if (nextIndex == CompositeDecoder.UNKNOWN_NAME) {
                skippedValues.add(nextKey)
                continue
            }

            return nextIndex.also { index = it }
        }

        return CompositeDecoder.DECODE_DONE
    }

    override fun decodeInt(): Int {
        return (decodeValue() as Long).toInt()
    }

    override fun decodeFloat(): Float {
        return (decodeValue() as Double).toFloat()
    }

    override fun decodeValue(): Any {
        return dataSnapshot.child(key!!).value!!
    }
}
