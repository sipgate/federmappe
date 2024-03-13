package de.sipgate.federmappe.realtimedb

import android.util.Log
import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class MapDecoder(
    private val list: List<DataSnapshot?>,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    private val ignoreUnknownProperties: Boolean = false,
) : AbstractDecoder() {
    private val keysIterator = list.iterator()
    private var index: Int = -2

    private val skippedValues = mutableSetOf<String>()

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = list.size

    override fun decodeValue(): Any = list[index]!!.value!!

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
       decodeEnum(enumDescriptor, ::decodeValue)

    override fun decodeNotNullMark(): Boolean = list[index] != null

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (keysIterator.hasNext()) {
            val nextKey = keysIterator.next()
            if (index >= 0) {
                if (index % 2 == 0) {
                    index += 1
                    return index
                }
            }
            val nextIndex =
                if (descriptor.kind == StructureKind.MAP) {
                    list.indexOf(nextKey)
                } else {
                    descriptor.getElementIndex(nextKey!!.key!!)
                }
            if (nextIndex == CompositeDecoder.UNKNOWN_NAME) {
                Log.w("MapDecoder", "encountered unknown key while decoding")
                skippedValues.add(nextKey!!.key!!)
                continue
            }

            index = nextIndex
            return nextIndex
        }

        return CompositeDecoder.DECODE_DONE
    }

    @Suppress("UNCHECKED_CAST")
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val value =
            if (index % 2 == 0) {
                list[index + 1]
            } else {
                list[index]
            }!!

        when (descriptor.kind) {
            StructureKind.CLASS -> {
                return SnapshotDecoder(
                    dataSnapshot = value,
                    ignoreUnknownProperties = ignoreUnknownProperties,
                    serializersModule = serializersModule,
                )
            }
            StructureKind.MAP -> {
                return MapDecoder(
                    list = value.children.map { it },
                    ignoreUnknownProperties = ignoreUnknownProperties,
                )
            }
            StructureKind.LIST -> {
                val list = value.children.map { it }
                return ListDecoder(ArrayDeque(list), list.size, serializersModule)
            }
            else -> throw SerializationException("Given value is neither a list nor a type $value")
        }
    }
}
