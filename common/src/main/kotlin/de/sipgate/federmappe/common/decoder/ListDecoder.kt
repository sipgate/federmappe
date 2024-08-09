package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlin.also
import kotlin.collections.toCollection

@ExperimentalSerializationApi
class ListDecoder(
    private val list: ArrayDeque<Any>,
    private val elementsCount: Int = 0,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    private val dataNormalizer: DataNormalizer
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

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        decodeEnum(enumDescriptor, ::decodeValue)

    override fun decodeNotNullMark(): Boolean =
        when {
            list.firstOrNull() != null -> true
            else -> false.also { list.removeFirst() }
        }

    @Suppress("UNCHECKED_CAST")
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val value = list.removeFirst()

        when (descriptor.kind) {
            StructureKind.CLASS -> return StringMapToObjectDecoder(
                data = value as Map<String, Any>,
                ignoreUnknownProperties = true,
                serializersModule = this.serializersModule,
                dataNormalizer = dataNormalizer
            )

            StructureKind.LIST -> {
                val subList = (value as Iterable<Any>).toCollection(mutableListOf())
                return ListDecoder(
                    list = ArrayDeque(subList),
                    elementsCount = subList.size,
                    serializersModule = serializersModule,
                    dataNormalizer = dataNormalizer
                )
            }

            else -> throw SerializationException("Type is not a list ${descriptor.serialName}")
        }
    }
}
