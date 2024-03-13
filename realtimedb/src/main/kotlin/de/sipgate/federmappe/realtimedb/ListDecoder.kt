package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class ListDecoder(
    private val list: ArrayDeque<DataSnapshot>,
    private val elementsCount: Int = 0,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
) : AbstractDecoder() {
    private var index = 0

    override fun decodeSequentially(): Boolean = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = elementsCount

    override fun decodeValue(): Any = list.removeFirst().value!!

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        when (index) {
            elementsCount -> CompositeDecoder.DECODE_DONE
            else -> index++
        }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        decodeEnum(enumDescriptor, ::decodeValue)

    override fun decodeNotNullMark(): Boolean =
        when {
            list.firstOrNull()?.value != null -> true
            else -> false.also { list.removeFirst() }
        }

    @Suppress("UNCHECKED_CAST")
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val value = list.removeFirst()

        when (descriptor.kind) {
            StructureKind.CLASS ->
                return SnapshotDecoder(
                    dataSnapshot = value,
                    ignoreUnknownProperties = true,
                    serializersModule = this.serializersModule,
                )
            StructureKind.LIST -> {
                val subList = (value as Iterable<DataSnapshot>).toCollection(mutableListOf())
                return ListDecoder(ArrayDeque(subList), subList.size, serializersModule)
            }
            else -> {}
        }

        return ListDecoder(list, descriptor.elementsCount)
    }
}
