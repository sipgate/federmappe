package de.sipgate.federmappe.common.decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractDecoder

/**
 * Converts a decoded number into the type the target field asks for, instead of casting it.
 *
 * A database picks the type per value: a whole number arrives as [Long], a fractional one as
 * [Double], and neither [Int] nor [Float] is ever handed out. [AbstractDecoder] casts the raw value,
 * so a field pinned to any one of those six types throws [ClassCastException] on every value that
 * came in as another — and that exception costs the whole document, not the field.
 *
 * Narrowing is silent, the same way `Long.toInt()` has always been here. A value that is not a
 * number at all keeps throwing [ClassCastException]: that is a broken document rather than a type
 * the database chose, and the exception it raises stays the one the decoders have always raised.
 */
@ExperimentalSerializationApi
abstract class NumberTolerantDecoder : AbstractDecoder() {
    private fun decodeNumber(): Number = decodeValue() as Number

    override fun decodeByte(): Byte = decodeNumber().toByte()

    override fun decodeShort(): Short = decodeNumber().toShort()

    override fun decodeInt(): Int = decodeNumber().toInt()

    override fun decodeLong(): Long = decodeNumber().toLong()

    override fun decodeFloat(): Float = decodeNumber().toFloat()

    override fun decodeDouble(): Double = decodeNumber().toDouble()
}
