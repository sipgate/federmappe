package de.sipgate.federmappe.common.helper

import java.util.SortedMap
import kotlin.collections.toSortedMap

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
