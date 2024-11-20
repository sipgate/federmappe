@file:Suppress("UNCHECKED_CAST")

package de.sipgate.federmappe.common

fun StringMap.dumpContents(indent: Int = 4, indentationChar: Char = ' '): String {

    val stringBuffer = StringBuilder()

    fun printNode(key: String, value: Any?, depth: Int = 0) {
        stringBuffer.append(indentationChar.toString().repeat(depth * indent))
        if (value is Map<*, *>) {
            stringBuffer.append(key).append(":\n")
            (value as? StringMap)?.forEach { printNode(it.key, it.value, depth + 1) }
        } else {
            stringBuffer.append("$key ($value)")
        }
        stringBuffer.append("\n")
    }

    forEach { printNode(it.key, it.value) }

    return stringBuffer.toString()
}
