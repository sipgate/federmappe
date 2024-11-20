package de.sipgate.federmappe.common

import kotlin.test.Test

class StringMapDumpContentsTest {
    @Test
    fun singleDataStructureDumpTest() {
        val stringMap = mapOf<String, Any?>(
            "test" to "string"
        )
        println(stringMap.dumpContents())
    }

    @Test
    fun multiDataStructureDumpTest() {
        val stringMap = mapOf<String, Any?>(
            "test" to "string",
            "other" to 15L
        )
        println(stringMap.dumpContents())
    }

    @Test
    fun nestedDataStructureDumpTest() {
        val stringMap = mapOf<String, Any?>(
            "test" to mapOf(
                "innerKey" to "innerValue"
            ),
            "other" to 15L
        )
        println(stringMap.dumpContents())
    }
}
