package de.sipgate.federmappe.firestore

import de.sipgate.federmappe.common.decoder.DataNormalizer

class FirestoreTimestampDataNormalizer : DataNormalizer {
    override fun normalize(data: Map<String, Any?>): Map<String, Any?> =
        data.normalizeStringMapNullable()
}
