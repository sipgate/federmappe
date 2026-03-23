package de.sipgate.federmappe.common.helper

import de.sipgate.federmappe.common.StringMap
import java.util.SortedMap
import kotlin.collections.toSortedMap

private val prioritizeTypeKey =  Comparator<String> { a, b ->
    when {
        (a == "type") -> -1
        (b == "type") -> 1
        else -> a.compareTo(b)
    }
}

internal fun StringMap.sortByPrio(): SortedMap<String, Any?> {
    return toSortedMap(prioritizeTypeKey)
}
