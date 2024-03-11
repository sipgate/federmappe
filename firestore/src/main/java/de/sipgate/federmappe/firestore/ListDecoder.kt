package de.sipgate.federmappe.firestore

import com.google.firebase.Timestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class ListDecoder(
    private val list: ArrayDeque<Any>,
    private val elementsCount: Int = 0,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
) : AbstractDecoder() {
    private var index = 0

    override fun decodeSequentially(): Boolean = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = elementsCount

    override fun decodeValue(): Any = list.removeFirst()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        when (index) {
            elementsCount -> CompositeDecoder.DECODE_DONE
            else -> index++
        }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeEnum(enumDescriptor, ::decodeValue)

    override fun decodeNotNullMark(): Boolean =
        when {
            list.firstOrNull() != null -> true
            else -> false.also { list.removeFirst() }
        }

    @Suppress("UNCHECKED_CAST")
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val value = list.removeFirst()
        if (value is Map<*, *>) {
            return StringMapToObjectDecoder(
                data = value as Map<String, Any>,
                ignoreUnknownProperties = true,
                serializersModule = this.serializersModule,
            )
        }

        if (value is Iterable<*>) {
            val subList = (value as Iterable<Any>).toCollection(mutableListOf())
            return ListDecoder(ArrayDeque(subList), subList.size, serializersModule)
        }

        if (value is Timestamp) {
            return FirebaseTimestampDecoder(timestamp = value)
        }

        return ListDecoder(list, descriptor.elementsCount)
    }
}
