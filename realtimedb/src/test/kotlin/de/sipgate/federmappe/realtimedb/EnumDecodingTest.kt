package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class EnumDecodingTest {
    enum class SomeEnum {
        A, B
    }

    @Test
    fun simpleEnumDecodingTest() {
        @Serializable
        data class Asdf(val someValue: SomeEnum)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns "A"
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
        assertEquals(SomeEnum.A, output.someValue)
    }

    enum class CustomEnum {
        @SerialName("avalue") A,
        @SerialName("bvalue") B
    }

    @Test
    fun customEnumDecodingTest() {
        @Serializable
        data class Asdf(val someValue: CustomEnum)

        val expectedProperty = mockk<DataSnapshot> {
            every { value } returns "avalue"
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
        assertEquals(CustomEnum.A, output.someValue)
    }
}
