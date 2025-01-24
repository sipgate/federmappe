package de.sipgate.federmappe.common.decoder

import de.sipgate.federmappe.common.StringMap
import de.sipgate.federmappe.common.helper.nextOrNull
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

@ExperimentalSerializationApi
class StringMapToObjectDecoder(
    private val data: StringMap,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    private val ignoreUnknownProperties: Boolean = false,
) : AbstractDecoder(), TypeAwareDecoder {
    private val keysIterator = data.sortByPrio().keys.iterator()
    private var index: Int? = null
    private var key: String? = null

    private val skippedValues = mutableSetOf<String>()

    override fun decodeSequentially(): Boolean {
        // We cannot guarantee the order in which the
        // elements are returned from the Database
        return false
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = data.size

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        val nextKey = keysIterator.nextOrNull().also { key = it }

        if (nextKey == null) {
            return CompositeDecoder.DECODE_DONE
        }

        val nextIndex = descriptor.getElementIndex(nextKey)
        if (nextIndex == CompositeDecoder.UNKNOWN_NAME) {
            skippedValues.add(nextKey)
            return decodeElementIndex(descriptor)
        }

        return nextIndex.also { index = it }
    }

    override fun decodeValue(): Any = data[key] ?: throw SerializationException("error decoding $key")

    override fun decodeNotNullMark(): Boolean = data[key] != null

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        decodeEnum(enumDescriptor, ::decodeValue)

    override fun decodeInt(): Int = (decodeValue() as Long).toInt()
    override fun decodeShort(): Short = (decodeValue() as Long).toShort()
    override fun decodeByte(): Byte = (decodeValue() as Long).toByte()
    override fun decodeFloat(): Float = (decodeValue() as Double).toFloat()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        if (descriptor.kind is PolymorphicKind.SEALED) {
            return StringMapToObjectDecoder(
                data = mapOf(
                    "type" to data["type"],
                    "value" to data
                ),
                serializersModule = serializersModule,
                ignoreUnknownProperties = ignoreUnknownProperties
            )
        }

        if (key == null) {
            return this
        }

        val value = data[key]
        val valueDescriptor = descriptor.kind

        @Suppress("UNCHECKED_CAST")
        when (valueDescriptor) {
            StructureKind.CLASS -> return StringMapToObjectDecoder(
                data = value as StringMap,
                ignoreUnknownProperties = ignoreUnknownProperties,
                serializersModule = this.serializersModule,
            )

            StructureKind.MAP -> return MapDecoder(
                map = value as StringMap,
                ignoreUnknownProperties = ignoreUnknownProperties,
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
        val currentData = (data[key] as? StringMap) ?: data
        return (currentData[typeKey] as? T)
    }
}
