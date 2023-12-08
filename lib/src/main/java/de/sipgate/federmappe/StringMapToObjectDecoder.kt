package de.sipgate.federmappe

import android.util.Log
import com.google.firebase.Timestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
class StringMapToObjectDecoder(
    private val data: Map<String, Any?>,
    override val serializersModule: SerializersModule = EmptySerializersModule(),
    private val ignoreUnknownProperties: Boolean = false,
) : AbstractDecoder() {
    private val keysIterator = data.keys.iterator()
    private var index: Int? = null
    var key: String? = null

    private val skippedValues = mutableSetOf<String>()

    override fun decodeSequentially() = false

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = data.size

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (keysIterator.hasNext()) {
            val nextKey = keysIterator.next().also { key = it }

            val nextIndex =
                when (descriptor.kind) {
                    StructureKind.MAP -> data.keys.indexOf(nextKey)
                    else -> descriptor.getElementIndex(nextKey)
                }

            if (nextIndex == CompositeDecoder.UNKNOWN_NAME) {
                Log.w("StringMapToObjectDecoder", "encountered unknown key while decoding $key")
                skippedValues.add(nextKey)
                continue
            }

            return nextIndex.also { index = it }
        }

        return CompositeDecoder.DECODE_DONE
    }

    override fun decodeValue(): Any = data[key] ?: throw SerializationException("error decoding $key")

    override fun decodeNotNullMark(): Boolean = data[key] != null

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeEnum(enumDescriptor, ::decodeValue)

    @Suppress("UNCHECKED_CAST")
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        if (key == null) {
            return this
        }

        val value = data[key]
        val valueDescriptor = descriptor.kind
        if (value is Map<*, *> && valueDescriptor == StructureKind.CLASS) {
            return StringMapToObjectDecoder(
                data = value as Map<String, Any>,
                ignoreUnknownProperties = ignoreUnknownProperties,
                serializersModule = this.serializersModule,
            )
        }

        if (value is Map<*, *> && valueDescriptor == StructureKind.MAP) {
            return MapDecoder(
                map = value as Map<String, Any>,
                ignoreUnknownProperties = ignoreUnknownProperties,
            )
        }

        if (value is Iterable<*>) {
            val list = (value as Iterable<Any>).toCollection(mutableListOf())
            return ListDecoder(ArrayDeque(list), list.size, serializersModule)
        }

        if (value is Timestamp) {
            return FirebaseTimestampDecoder(
                timestamp = value,
            )
        }

        throw SerializationException("Given value is neither a list nor a type $value")
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (skippedValues.isNotEmpty() && !ignoreUnknownProperties) {
            throw SerializationException("found unhandled properties: $skippedValues")
        }

        super.endStructure(descriptor)
    }
}
