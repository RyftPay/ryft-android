package com.ryftpay.android.ui.extension

private val TITLES = arrayOf("MISS", "MR", "MRS", "MS")

// Returns a string of digits if all characters are digits, else null
internal fun String.toDigitsOrNull(): String? = if (!any() || any { !it.isDigit() }) {
    null
} else this

// Overloaded method that also filters out any separators in the string
internal fun String.toDigitsOrNull(filterSeparator: Char): String? =
    filter { it != filterSeparator }.toDigitsOrNull()

// Adds a separator character to the given positions in the string
internal fun String.addSeparatorIntoPositions(separator: Char, positions: IntArray): String =
    if (!positions.any()) {
        this
    } else this.mapIndexed { index, c ->
        val position = index + 1
        if (positions.contains(position)) {
            "$c$separator"
        } else {
            c.toString()
        }
    }.joinToString(separator = "")

internal fun String?.extractFirstAndLastNamesOrNulls(): Pair<String?, String?> {
    if (this == null) {
        return Pair(null, null)
    }
    val names = this.split(" ").filter { !TITLES.contains(it.uppercase()) }
    val firstName = names.firstOrNull()?.ifEmpty { null }
    val lastName = names.drop(1).lastOrNull()?.ifEmpty { null }
    return Pair(
        firstName,
        lastName
    )
}

internal fun String.numberOfWords(): Int = this.trim().split(' ').size
