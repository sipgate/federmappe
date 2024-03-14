package de.sipgate.federmappe.common

interface TypeAwareDecoder {
    fun <T> decodeType(typeKey: String = "type"): T?
}
