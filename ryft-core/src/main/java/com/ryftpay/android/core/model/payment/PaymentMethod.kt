package com.ryftpay.android.core.model.payment

data class PaymentMethod(
    val type: PaymentMethodType,
    val cardDetails: CardDetails?,
    val googlePayToken: String?
) {
    companion object {
        fun card(cardDetails: CardDetails) = PaymentMethod(
            type = PaymentMethodType.Card,
            cardDetails = cardDetails,
            googlePayToken = null
        )

        fun googlePay(googlePayToken: String) = PaymentMethod(
            type = PaymentMethodType.GooglePay,
            cardDetails = null,
            googlePayToken = googlePayToken
        )
    }
}
