package com.ryftpay.android.ui.model

import com.ryftpay.android.ui.extension.numberOfWords

internal class RyftCardName private constructor(
    internal val sanitisedName: String
) {
    internal val validationState = validate()

    private fun validate(): ValidationState = when {
        sanitisedName.numberOfWords() < MIN_NUMBER_OF_WORDS -> {
            ValidationState.Incomplete
        }
        sanitisedName.length > MAX_LENGTH -> {
            ValidationState.Invalid
        }
        else -> ValidationState.Valid
    }

    companion object {
        internal const val MAX_LENGTH = 120
        private const val MIN_NUMBER_OF_WORDS = 2
        internal val Incomplete = from(inputName = null)

        internal fun from(inputName: String?): RyftCardName {
            return RyftCardName(sanitisedName = inputName?.trim() ?: "")
        }
    }
}
