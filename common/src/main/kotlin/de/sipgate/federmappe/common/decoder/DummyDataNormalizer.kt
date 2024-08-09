package de.sipgate.federmappe.common.decoder

class DummyDataNormalizer : DataNormalizer {
    override fun normalize(data: Map<String, Any?>): Map<String, Any?> = data
}
