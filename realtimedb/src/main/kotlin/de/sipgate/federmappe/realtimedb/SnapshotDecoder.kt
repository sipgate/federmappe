package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class SnapshotDecoder(
    private val dataSnapshot: DataSnapshot,
    private val ignoreUnknownProperties: Boolean,
    override val serializersModule: SerializersModule,
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

            val nextIndex = descriptor.getElementIndex(nextKey)
            if (nextIndex == CompositeDecoder.UNKNOWN_NAME) {
                skippedValues.add(nextKey)
                continue
            }

            return nextIndex.also { index = it }
        }

        return CompositeDecoder.DECODE_DONE
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        if (key == null) return this

        val value = dataSnapshot.child(key!!)
        val valueDescriptor = descriptor.kind
        when (valueDescriptor) {
            StructureKind.CLASS -> {
                return SnapshotDecoder(
                    dataSnapshot = value,
                    ignoreUnknownProperties = ignoreUnknownProperties,
                    serializersModule = this.serializersModule,
                )
            }

            PolymorphicKind.SEALED -> {
                return PolymorphicDecoder(
                    dataSnapshot = value,
                    ignoreUnknownProperties = ignoreUnknownProperties,
                    serializersModule = this.serializersModule,
                    type = descriptor.serialName
                )
            }

            StructureKind.MAP -> {
                return MapDecoder(
                    list = value.children.map { it },
                    ignoreUnknownProperties = ignoreUnknownProperties,
                    serializersModule = serializersModule,
                )
            }

            StructureKind.LIST -> {
                val list = value.children.map { it }
                return ListDecoder(ArrayDeque(list), list.size, serializersModule)
            }

            else -> throw SerializationException(
                "Given value is neither a list nor a type! value: $value, type: ${value::class.qualifiedName}"
            )
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (skippedValues.isNotEmpty() && !ignoreUnknownProperties) {
            throw SerializationException("found unhandled properties: $skippedValues")
        }

        super.endStructure(descriptor)
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        return decodeEnum(enumDescriptor, ::decodeValue)
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

    fun decodeValue(descriptor: SerialDescriptor, index: Int): Any {
        val key = descriptor.getElementName(index)
        return dataSnapshot.child(key).value!!
    }

//    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
//        return deserializer.deserialize(SnapshotDecoder(
//            dataSnapshot= decodeValue() as DataSnapshot,
//            ignoreUnknownProperties = ignoreUnknownProperties,
//            serializersModule = serializersModule
//        ))
//    }
}
