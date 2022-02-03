package com.ryftpay.android.ui.extension

// Returns a string of digits if all characters are digits, else null
internal fun String.toDigitsOrNull(): String? {
    return if (!any() || any { !it.isDigit() }) {
        null
    } else this
}

// Overloaded method that also filters out any separators in the string
internal fun String.toDigitsOrNull(filterSeparator: Char): String? =
    filter { it != filterSeparator }.toDigitsOrNull()

// Adds a separator character to the given positions in the string
internal fun String.addSeparatorIntoPositions(separator: Char, positions: IntArray): String {
    if (!positions.any()) {
        return this
    }
    return this.mapIndexed { index, c ->
        val position = index + 1
        if (positions.contains(position)) {
            "$c$separator"
        } else {
            c.toString()
        }
    }.joinToString(separator = "")
}
