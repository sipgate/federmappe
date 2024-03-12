package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class FractionalNumberTests {
    @Test
    fun floatDecodingTest() {
        @Serializable
        data class Asdf(val someValue: Float)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns 1.5
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
        Assertions.assertEquals(1.5f, output.someValue)
    }

    @Test
    fun doubleDecodingTest() {
        @Serializable
        data class Asdf(val someValue: Double)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns 1.5
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
        Assertions.assertEquals(1.5, output.someValue)
    }
}
