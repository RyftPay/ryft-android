package com.ryftpay.android.ui.model

internal data class RyftCard(
    internal val number: RyftCardNumber,
    internal val expiryDate: RyftCardExpiryDate,
    internal val cvc: RyftCardCvc,
    internal val name: RyftCardName?,
    internal val type: RyftCardType
) {
    internal val valid: Boolean =
        number.validationState == ValidationState.Valid &&
            expiryDate.validationState == ValidationState.Valid &&
            cvc.validationState == ValidationState.Valid &&
            (name == null || name.validationState == ValidationState.Valid)

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

    internal fun withName(name: RyftCardName): RyftCard = copy(
        name = name
    )

    companion object {
        internal fun incomplete(collectNameOnCard: Boolean): RyftCard = RyftCard(
            RyftCardNumber.Incomplete,
            RyftCardExpiryDate.Incomplete,
            RyftCardCvc.Incomplete,
            if (collectNameOnCard) RyftCardName.Incomplete else null,
            RyftCardNumber.Incomplete.derivedType
        )
    }
}
