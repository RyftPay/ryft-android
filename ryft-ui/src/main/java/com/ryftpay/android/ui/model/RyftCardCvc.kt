package com.ryftpay.android.ui.model

import com.ryftpay.android.ui.extension.toDigitsOrNull

internal class RyftCardCvc private constructor(
    internal val sanitisedCvc: String,
    cardType: RyftCardType
) {
    internal val validationState = validate(cardType)

    private fun validate(cardType: RyftCardType): ValidationState = when {
        sanitisedCvc.length < cardType.cvcLength || cardType == RyftCardType.Unknown -> {
            ValidationState.Incomplete
        }
        sanitisedCvc.length > cardType.cvcLength -> {
            ValidationState.Invalid
        }
        else -> ValidationState.Valid
    }

    companion object {
        internal val Incomplete = from(inputCvc = null, cardType = RyftCardType.Unknown)

        internal fun from(inputCvc: String?, cardType: RyftCardType): RyftCardCvc {
            return RyftCardCvc(inputCvc?.toDigitsOrNull() ?: "", cardType)
        }
    }
}
