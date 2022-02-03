package com.ryftpay.android.ui.model

import com.ryftpay.android.ui.extension.addSeparatorIntoPositions
import com.ryftpay.android.ui.extension.toDigitsOrNull

internal class RyftCardNumber private constructor(
    internal val sanitisedNumber: String = ""
) {
    internal val derivedType: RyftCardType = RyftCardType.fromCardNumber(sanitisedNumber)
    internal val validationState: ValidationState = validate()

    internal fun formatted(removeTrailingSeparator: Boolean): String {
        val formattedCardNumber = sanitisedNumber
            .addSeparatorIntoPositions(SEPARATOR, derivedType.cardNumberFormatGaps)
            .take(derivedType.maxFormattedCardLength)
        return if (removeTrailingSeparator) {
            formattedCardNumber.trimEnd {
                it == SEPARATOR
            }
        } else {
            formattedCardNumber
        }
    }

    private fun validate(): ValidationState {
        if (derivedType.cardLengths.contains(sanitisedNumber.length) && derivedType != RyftCardType.Unknown) {
            return if (luhnCheck()) ValidationState.Valid else ValidationState.Invalid
        }
        if (sanitisedNumber.length < (derivedType.cardLengths.maxOrNull() ?: 0)) {
            return ValidationState.Incomplete
        }
        return ValidationState.Invalid
    }

    private fun luhnCheck() =
        sanitisedNumber.reversed().mapIndexed { idx, value ->
            val digit = value.digitToInt()
            if (idx % 2 == 1) (digit * 2).toString().sumOf { it - '0' } else digit
        }.sum() % 10 == 0

    companion object {
        private const val SEPARATOR = ' '
        internal val Incomplete = from(inputCardNumber = null)

        internal fun from(inputCardNumber: String? = null): RyftCardNumber {
            return RyftCardNumber(inputCardNumber?.toDigitsOrNull(SEPARATOR) ?: "")
        }
    }
}
