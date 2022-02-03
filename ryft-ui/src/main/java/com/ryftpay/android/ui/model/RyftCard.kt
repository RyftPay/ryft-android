package com.ryftpay.android.ui.model

internal data class RyftCard(
    internal val number: RyftCardNumber,
    internal val expiryDate: RyftCardExpiryDate,
    internal val cvc: RyftCardCvc,
    internal val type: RyftCardType
) {
    internal val valid: Boolean =
        number.validationState == ValidationState.Valid &&
            expiryDate.validationState == ValidationState.Valid &&
            cvc.validationState == ValidationState.Valid

    internal fun withCardNumber(cardNumber: RyftCardNumber): RyftCard = copy(
        number = cardNumber,
        type = cardNumber.derivedType
    )

    internal fun withExpiryDate(expiryDate: RyftCardExpiryDate): RyftCard = copy(
        expiryDate = expiryDate
    )

    internal fun withCvc(cvc: RyftCardCvc): RyftCard = copy(
        cvc = cvc
    )

    companion object {
        internal val Incomplete = RyftCard(
            RyftCardNumber.Incomplete,
            RyftCardExpiryDate.Incomplete,
            RyftCardCvc.Incomplete,
            RyftCardNumber.Incomplete.derivedType
        )
    }
}
