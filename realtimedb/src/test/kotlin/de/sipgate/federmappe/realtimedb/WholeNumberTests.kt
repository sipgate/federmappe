package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class WholeNumberTests {
    @Test
    fun integerDecodingTest() {
        @Serializable
        data class Asdf(val someValue: Int)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns 1234L
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
        Assertions.assertEquals(1234, output.someValue)
    }

    @Test
    fun longDecodingTest() {
        @Serializable
        data class Asdf(val someValue: Long)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns 1234L
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
        Assertions.assertEquals(1234L, output.someValue)
    }
}