package de.sipgate.federmappe.common.decoder

import de.sipgate.federmappe.common.helper.sortByPrio
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlin.also
import kotlin.collections.isNotEmpty
import kotlin.collections.toCollection

@ExperimentalSerializationApi
class StringMapToObjectDecoder(
    private val data: Map<String, Any?>,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    private val ignoreUnknownProperties: Boolean = false,
    private val dataNormalizer: DataNormalizer = DummyDataNormalizer()
) : AbstractDecoder(), TypeAwareDecoder {
    private val keysIterator = data.sortByPrio().keys.iterator()
    private var index: Int? = null
    private var key: String? = null

    private val skippedValues = mutableSetOf<String>()

    override fun decodeSequentially() = false

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = data.size

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (keysIterator.hasNext()) {
            val nextKey = keysIterator.next().also { key = it }

            val nextIndex = descriptor.getElementIndex(nextKey)
            if (nextIndex == CompositeDecoder.UNKNOWN_NAME) {
                skippedValues.add(nextKey)
                continue
            }

            return nextIndex.also { index = it }
        }

        return CompositeDecoder.DECODE_DONE
    }

    override fun decodeValue(): Any = data[key] ?: throw SerializationException("error decoding $key")

    override fun decodeNotNullMark(): Boolean = data[key] != null

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        decodeEnum(enumDescriptor, ::decodeValue)

    override fun decodeInt(): Int = (decodeValue() as Long).toInt()
    override fun decodeShort(): Short = (decodeValue() as Long).toShort()
    override fun decodeByte(): Byte = (decodeValue() as Long).toByte()
    override fun decodeFloat(): Float = (decodeValue() as Double).toFloat()

    @Suppress("UNCHECKED_CAST")
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        if (key == null) {
            return this
        }

        val normalizedData = dataNormalizer.normalize(data)
        val value = normalizedData[key]
        val valueDescriptor = descriptor.kind

        when (valueDescriptor) {
            StructureKind.CLASS -> return StringMapToObjectDecoder(
                data = value as Map<String, Any>,
                ignoreUnknownProperties = ignoreUnknownProperties,
                serializersModule = this.serializersModule,
                dataNormalizer = dataNormalizer
            )

            PolymorphicKind.SEALED -> return StringMapToObjectDecoder(
                data = value as Map<String, Any>,
                ignoreUnknownProperties = ignoreUnknownProperties,
                serializersModule = this.serializersModule,
                dataNormalizer = dataNormalizer
            )

            StructureKind.MAP -> return MapDecoder(
                map = value as Map<String, Any>,
                ignoreUnknownProperties = ignoreUnknownProperties,
                dataNormalizer = dataNormalizer
            )

            StructureKind.LIST -> {
                val list = when (value) {
                    is Iterable<*> -> (value as Iterable<Any>).toCollection(mutableListOf())
                    is Map<*, *> -> (value.values as Iterable<Any>).toCollection(mutableListOf())
                    else -> emptyList<Any>()
                }

                return ListDecoder(
                    list = ArrayDeque(list),
                    elementsCount = list.size,
                    serializersModule = serializersModule,
                    dataNormalizer = dataNormalizer
                )
            }

            else -> throw SerializationException("${key ?: "root"} was expected to be of type $valueDescriptor, but was $value")
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (skippedValues.isNotEmpty() && !ignoreUnknownProperties) {
            throw SerializationException("found unhandled properties: $skippedValues")
        }

        super.endStructure(descriptor)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> decodeType(typeKey: String): T? {
        val currentData = (data[key] as? Map<String, Any>) ?: data
        return (currentData[typeKey] as? T)
    }
}
