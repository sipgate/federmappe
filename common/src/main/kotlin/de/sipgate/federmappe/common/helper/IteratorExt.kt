package de.sipgate.federmappe.common.helper

fun <T> Iterator<T>.nextOrNull(): T? {
    return when (hasNext()) {
        true -> next()
        else -> null
    }
}
