package de.sipgate.federmappe.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MapStringAnyExtTest {
    @Test
    fun mapThatBeginsWithTypeIsReturnedAsIs() {
        val input = mapOf(
            "type" to "typeValue",
            "payload" to "payloadValue"
        )

        val result = input.sortByPrio()

        assertEquals("type", input.keys.first())
        assertEquals("type", result.keys.first())
        assertEquals(input.keys, result.keys)
    }

    @Test
    fun mapThatDoesNotBeginWithTypeIsReorderd() {
        val input = mapOf(
            "payload" to "payloadValue",
            "type" to "typeValue"
        )

        val result = input.sortByPrio()

        assertEquals("payload", input.keys.first())
        assertEquals("type", result.keys.first())
        assertEquals(input.keys, result.keys)
    }
}