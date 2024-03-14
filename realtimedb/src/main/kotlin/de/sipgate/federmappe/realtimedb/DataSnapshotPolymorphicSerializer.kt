package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KClass

@InternalSerializationApi
abstract class DataSnapshotPolymorphicSerializer<T : Any>(private val baseClass: KClass<T>) :
        KSerializer<T> {

        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("DataSnapshotPolymorphicSerializer<${baseClass.simpleName}>", PolymorphicKind.SEALED)

        final override fun serialize(encoder: Encoder, value: T) {
            val actualSerializer =
                encoder.serializersModule.getPolymorphic(baseClass, value)
                    ?: value::class.serializerOrNull()
                    ?: throwSubtypeNotRegistered(value::class, baseClass)
            @Suppress("UNCHECKED_CAST")
            (actualSerializer as KSerializer<T>).serialize(encoder, value)
        }

        final override fun deserialize(decoder: Decoder): T {
            val input = decoder as PolymorphicDecoder
            val tree = input.decodeValue() as DataSnapshot

            @Suppress("UNCHECKED_CAST")
            val actualSerializer = selectDeserializer(tree) as KSerializer<T>
            return input.decodeSerializableValue(actualSerializer)
        }

        protected abstract fun selectDeserializer(element: DataSnapshot): DeserializationStrategy<T>

        private fun throwSubtypeNotRegistered(subClass: KClass<*>, baseClass: KClass<*>): Nothing {
            val subClassName = subClass.simpleName ?: "$subClass"
            val scope = "in the scope of '${baseClass.simpleName}'"
            throw SerializationException(
                "Class '${subClassName}' is not registered for polymorphic serialization $scope.\n" +
                    "Mark the base class as 'sealed' or register the serializer explicitly.")
        }

    }
