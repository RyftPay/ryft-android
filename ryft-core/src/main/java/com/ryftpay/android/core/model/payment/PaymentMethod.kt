package com.ryftpay.android.core.model.payment

data class PaymentMethod(
    val type: PaymentMethodType,
    val cardDetails: CardDetails?,
    val googlePayToken: String?,
    val billingAddress: Address?,
    val options: PaymentMethodOptions?
) {
    companion object {
        fun card(cardDetails: CardDetails, options: PaymentMethodOptions) = PaymentMethod(
            type = PaymentMethodType.Card,
            cardDetails,
            googlePayToken = null,
            billingAddress = null,
            options
        )

        fun googlePay(googlePayToken: String, billingAddress: Address?) = PaymentMethod(
            type = PaymentMethodType.GooglePay,
            cardDetails = null,
            googlePayToken,
            billingAddress,
            options = null
        )
    }
}
