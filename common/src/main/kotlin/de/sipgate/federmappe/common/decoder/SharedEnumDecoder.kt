package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor

@ExperimentalSerializationApi
fun decodeEnum(
    enumDescriptor: SerialDescriptor,
    decodeValue: () -> Any,
): Int {
    val decodedValue = decodeValue()
    val enumNamespace = enumDescriptor.serialName

    for (i in 0..<enumDescriptor.elementsCount) {
        val enumValue = enumDescriptor.getElementDescriptor(i).serialName
        if (enumValue == "$enumNamespace.$decodedValue") {
            return i
        }
    }

    throw SerializationException("Couldn't find matching value for enum $enumNamespace")
}
