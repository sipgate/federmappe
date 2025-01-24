@file:Suppress("UNCHECKED_CAST")

package de.sipgate.federmappe.common

fun StringMap.dumpContents(indent: Int = 4, indentationChar: Char = ' ') = buildString {

    fun printNode(key: String, value: Any?, depth: Int = 0) {
        append(indentationChar.toString().repeat(depth * indent))
        if (value is Map<*, *>) {
            append(key).append(":\n")
            (value as? StringMap)?.forEach { printNode(it.key, it.value, depth + 1) }
        } else {
            append("$key ($value)")
        }
        append("\n")
    }

    this@dumpContents.forEach { printNode(it.key, it.value) }
}
