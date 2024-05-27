package de.sipgate.federmappe.common.decoder

interface TypeAwareDecoder {
    fun <T> decodeType(typeKey: String = "type"): T?
}
