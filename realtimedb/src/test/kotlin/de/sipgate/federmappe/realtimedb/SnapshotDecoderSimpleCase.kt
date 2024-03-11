package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import io.mockk.mockk
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SnapshotDecoderSimpleCase {
    @Test
    fun asdf() {
        @Serializable
        data class Asdf(val someValue: String)

        val data = mockk<DataSnapshot> {

        }

        val output = data.toObjectWithSerializer(Asdf.serializer())

        assertNotNull(output)
        assertEquals("asdf", output.someValue)
    }
}
