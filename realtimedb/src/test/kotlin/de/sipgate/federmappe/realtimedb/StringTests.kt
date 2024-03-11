package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class StringTests {
    @Test
    fun testStringWithMatchingName() {
        @Serializable
        data class Asdf(val someValue: String)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns "asdf"
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

        assertNotNull(output)
        assertEquals("asdf", output.someValue)
    }

    @Test
    fun testStringWithOverriddenName() {
        @Serializable
        data class Asdf(@SerialName("overriddenName") val someValue: String)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns "asdf"
            every { key } returns "overriddenName"
            every { children } returns emptyList()
        }

        val data = mockk<DataSnapshot> {
            every { key } returns null
            every { value } returns null
            every { children } returns listOf(expectedProperty)
            every { child("overriddenName") } returns expectedProperty
        }

        val output = data.toObjectWithSerializer(Asdf.serializer())

        assertNotNull(output)
        assertEquals("asdf", output.someValue)
    }
}
