package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BooleanDecodingTest {
    @Test
    fun nonNullBooleanTest() {
        @Serializable
        data class Asdf(val someValue: Boolean)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns true
            every { key } returns "someValue"
            every { children } returns emptyList()
        }

        val data = mockk<DataSnapshot> {
            every { key } returns null
            every { value } returns null
            every { children } returns listOf(expectedProperty)
            every { child("someValue") } returns expectedProperty
        }

        val output = data.toObjectWithSerializer(Asdf.serializer())

        Assertions.assertNotNull(output)
        Assertions.assertTrue(output.someValue)
    }

    @Test
    fun nullableBooleanTest() {
        @Serializable
        data class Asdf(val someValue: Boolean? = null)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns true
            every { key } returns "someValue"
            every { children } returns emptyList()
        }

        val data = mockk<DataSnapshot> {
            every { key } returns null
            every { value } returns null
            every { children } returns listOf(expectedProperty)
            every { child("someValue") } returns expectedProperty
        }

        val output = data.toObjectWithSerializer(Asdf.serializer())

        Assertions.assertNotNull(output)
        Assertions.assertTrue(output.someValue!!)
    }

    @Test
    fun nullableBooleanDefaultsToNullTest() {
        @Serializable
        data class Asdf(val someValue: Boolean? = null)

        val data = mockk<DataSnapshot> {
            every { key } returns null
            every { value } returns null
            every { children } returns emptyList()
        }

        val output = data.toObjectWithSerializer(Asdf.serializer())

        Assertions.assertNotNull(output)
        Assertions.assertNull(output.someValue)
    }
}
