package de.sipgate.federmappe.common

import java.util.SortedMap

private val prioritizeTypeKey =  Comparator<String> { a, b ->
    when {
        (a == "type") -> -1
        (b == "type") -> 1
        else -> a.compareTo(b)
    }
}

internal fun Map<String, Any?>.sortByPrio(): SortedMap<String, Any?> {
    return toSortedMap(prioritizeTypeKey)
}
