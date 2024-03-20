package de.sipgate.federmappe.common

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
    private val map: Map<String, Any?>,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    private val ignoreUnknownProperties: Boolean = false,
) : AbstractDecoder() {
    private val flattenedData =
        map.entries.fold(emptyList<Any?>()) { acc, (key, value) ->
            acc + listOf(key, value)
        }

    private val keysIterator = flattenedData.iterator()
    private var index: Int = -2

    private val skippedValues = mutableSetOf<String>()

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = map.size

    override fun decodeValue(): Any = flattenedData[index] ?: throw SerializationException("error decoding")

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        decodeEnum(enumDescriptor, ::decodeValue)

    override fun decodeNotNullMark(): Boolean = flattenedData[index] != null

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
                    flattenedData.indexOf(nextKey)
                } else {
                    descriptor.getElementIndex(nextKey as String)
                }
            if (nextIndex == CompositeDecoder.UNKNOWN_NAME) {
                skippedValues.add(nextKey as String)
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
                flattenedData[index + 1]
            } else {
                flattenedData[index]
            }

        val valueDescriptor = descriptor.kind


        when (valueDescriptor) {
            StructureKind.CLASS -> return StringMapToObjectDecoder(
                data = value as Map<String, Any>,
                ignoreUnknownProperties = ignoreUnknownProperties,
                serializersModule = this.serializersModule,
            )

            StructureKind.MAP -> return MapDecoder(
                map = value as Map<String, Any>,
                ignoreUnknownProperties = ignoreUnknownProperties,
            )

            StructureKind.LIST -> {
                val list = (value as Iterable<Any>).toCollection(mutableListOf())
                return ListDecoder(ArrayDeque(list), list.size, serializersModule)
            }

            else -> throw SerializationException("Given value is neither a list nor a type $value")
        }
    }
}
