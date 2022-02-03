package com.ryftpay.android.ui.model

import com.ryftpay.android.ui.extension.addSeparatorIntoPositions
import com.ryftpay.android.ui.extension.toDigitsOrNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.max

internal class RyftCardExpiryDate private constructor(
    internal val sanitisedExpiryDate: String = ""
) {
    internal val twoDigitMonth = sanitisedExpiryDate.take(MONTH_LENGTH)
    internal val twoDigitYear = sanitisedExpiryDate.takeLast(
        max(sanitisedExpiryDate.length - YEAR_LENGTH, 0)
    )
    internal val fourDigitYear = if (twoDigitYear.length != YEAR_LENGTH) "" else CENTURY_PREFIX + twoDigitYear
    internal val validationState = validate()

    internal fun formatted(removeTrailingSeparator: Boolean): String {
        val formattedExpiryDate = sanitisedExpiryDate.addSeparatorIntoPositions(
            SEPARATOR,
            SeparatorPositions
        )
        return if (removeTrailingSeparator) {
            formattedExpiryDate.trimEnd {
                it == SEPARATOR
            }
        } else {
            formattedExpiryDate
        }
    }

    private fun validate(): ValidationState {
        if (sanitisedExpiryDate.length > MAX_LENGTH) {
            return ValidationState.Invalid
        }
        val monthValidationState = validateMonth(twoDigitMonth)
        if (monthValidationState == ValidationState.Invalid) {
            return monthValidationState
        }
        return validateYear(twoDigitYear, twoDigitMonth)
    }

    private fun validateMonth(month: String) = when (month.length) {
        0 -> ValidationState.Incomplete
        1 -> if (month[0].digitToInt() in 0..1) ValidationState.Incomplete else ValidationState.Invalid
        2 -> if (month.toInt() in 1..12) ValidationState.Incomplete else ValidationState.Invalid
        else -> ValidationState.Invalid
    }

    private fun validateYear(
        year: String,
        month: String
    ): ValidationState {
        val currentTime = Calendar.getInstance()
        val currentYear = currentTime.get(Calendar.YEAR) % 100
        val currentDecade = currentYear.toString()[0].digitToInt()
        val currentMonth = currentTime.get(Calendar.MONTH) + 1
        return when (year.length) {
            0 -> ValidationState.Incomplete
            1 -> {
                if (year[0].digitToInt() >= currentDecade) {
                    ValidationState.Incomplete
                } else ValidationState.Invalid
            }
            2 -> {
                if (year.toInt() > currentYear) {
                    ValidationState.Valid
                } else {
                    return if (year.toInt() == currentYear &&
                        month.toInt() >= currentMonth
                    ) {
                        ValidationState.Valid
                    } else ValidationState.Invalid
                }
            }
            else -> ValidationState.Invalid
        }
    }

    companion object {
        private const val SEPARATOR = '/'
        private const val MONTH_LENGTH = 2
        private const val YEAR_LENGTH = 2
        private const val MAX_LENGTH = MONTH_LENGTH + YEAR_LENGTH
        private const val DATE_FORMAT_STRING = "MM/yy"
        private const val CENTURY_PREFIX = "20"
        private val SeparatorPositions = intArrayOf(2)
        internal const val MAX_FORMATTED_LENGTH = SEPARATOR.toString().length + MONTH_LENGTH + YEAR_LENGTH
        internal val DateFormat = SimpleDateFormat(DATE_FORMAT_STRING, Locale.ENGLISH)
        internal val Incomplete = from(inputExpiryDate = null)

        internal fun from(inputExpiryDate: String?): RyftCardExpiryDate {
            val sanitisedExpiryDate = inputExpiryDate?.toDigitsOrNull(SEPARATOR) ?: ""
            return RyftCardExpiryDate(
                sanitisedExpiryDate
            )
        }
    }
}
