package de.sipgate.federmappe.common

import de.sipgate.federmappe.common.decoder.TypeAwareDecoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
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

@ExperimentalSerializationApi
@InternalSerializationApi
abstract class SealedClassWithTypeSerializer<Type : Any, DiscriminatorType>(
    private val baseClass: KClass<Type>,
    private val discriminatorName: String = "type"
) : KSerializer<Type> {

    override val descriptor: SerialDescriptor =
        buildSerialDescriptor(
            "JsonContentPolymorphicSerializer<${baseClass.simpleName}>",
            PolymorphicKind.SEALED
        )

    final override fun serialize(encoder: Encoder, value: Type) {
        val actualSerializer =
            encoder.serializersModule.getPolymorphic(baseClass, value)
                ?: value::class.serializerOrNull()
                ?: throwSubtypeNotRegistered(value::class, baseClass)
        @Suppress("UNCHECKED_CAST")
        (actualSerializer as KSerializer<Type>).serialize(encoder, value)
    }

    final override fun deserialize(decoder: Decoder): Type {
        val type = (decoder as? TypeAwareDecoder)
            ?.decodeType<DiscriminatorType>(typeKey = discriminatorName)
            ?: throw IllegalStateException("We need to know the type to decode first!")

        val actualSerializer = selectDeserializer(type) as KSerializer<Type>
        return decoder.decodeSerializableValue(actualSerializer)
    }

    protected abstract fun selectDeserializer(element: DiscriminatorType): DeserializationStrategy<Type>

    private fun throwSubtypeNotRegistered(subClass: KClass<*>, baseClass: KClass<*>): Nothing {
        val subClassName = subClass.simpleName ?: "$subClass"
        val scope = "in the scope of '${baseClass.simpleName}'"
        throw SerializationException(
            "Class '${subClassName}' is not registered for polymorphic serialization $scope.\n" +
                "Mark the base class as 'sealed' or register the serializer explicitly."
        )
    }
}
