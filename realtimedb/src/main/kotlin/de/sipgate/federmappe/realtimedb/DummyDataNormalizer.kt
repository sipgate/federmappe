package de.sipgate.federmappe.realtimedb

import de.sipgate.federmappe.common.decoder.DataNormalizer

class DummyDataNormalizer : DataNormalizer {
    override fun normalize(data: Map<String, Any?>): Map<String, Any?> = data
}
