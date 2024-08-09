package de.sipgate.federmappe.common.decoder

interface DataNormalizer {
    fun normalize(data: Map<String, Any?>): Map<String, Any?>
}
