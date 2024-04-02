package de.sipgate.federmappe.common

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MapStringAnyExtTest {
    @Test
    fun mapThatBeginsWithTypeIsReturnedAsIs() {
        val sortedMap = linkedMapOf(
            "type" to "typeValue",
            "payload" to "payloadValue"
        )

        val input = sortedMap.toMap()
        val result = input.sortByPrio()

        Assertions.assertEquals("type", result.keys.first())
        Assertions.assertEquals(sortedMap.keys, result.keys)
    }

    @Test
    fun mapThatDoesNotBeginWithTypeIsReorderd() {
        val sortedMap = linkedMapOf(
            "payload" to "payloadValue",
            "type" to "typeValue"
        )

        val input = sortedMap.toMap()
        val result = input.sortByPrio()

        Assertions.assertEquals("type", result.keys.first())
        Assertions.assertEquals(sortedMap.keys, result.keys)
    }
}
