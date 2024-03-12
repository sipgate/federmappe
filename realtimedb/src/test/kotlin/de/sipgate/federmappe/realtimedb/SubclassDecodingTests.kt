package de.sipgate.federmappe.realtimedb

import com.google.firebase.database.DataSnapshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
@OptIn(ExperimentalSerializationApi::class)
class SubclassDecodingTests {

    @Serializable
    data class A(val b: String)

    @Test
    fun nestedDataStructureDecodingTest() {
        @Serializable
        data class TestClass(val a: A)

        val bPropertyMock = mockk<DataSnapshot> {
            every { value } returns "Some String"
            every { key } returns "b"
            every { children } returns emptyList()
        }

        val aPropertyMock = mockk<DataSnapshot> {
            every { value } returns null
            every { key } returns "a"
            every { children } returns emptyList()
        }

        val testClassMock = mockk<DataSnapshot> {
            every { key } returns null
            every { value } returns null
            every { children } returns listOf(aPropertyMock)
            every { child("a") } returns aPropertyMock
        }

        val result = testClassMock.toObjectWithSerializer(TestClass.serializer())

        assertInstanceOf(TestClass::class.java, result)
        assertNotNull(result.a)
        assertEquals("Some String", result.a.b)
    }
}
