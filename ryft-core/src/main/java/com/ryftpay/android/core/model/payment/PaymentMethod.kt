package com.ryftpay.android.core.model.payment

data class PaymentMethod(
    val type: PaymentMethodType,
    val cardDetails: CardDetails?,
    val id: String?,
    val googlePayToken: String?,
    val billingAddress: Address?,
    val options: PaymentMethodOptions?
) {
    companion object {
        fun card(cardDetails: CardDetails, options: PaymentMethodOptions) = PaymentMethod(
            type = PaymentMethodType.Card,
            cardDetails,
            id = null,
            googlePayToken = null,
            billingAddress = null,
            options
        )

        fun id(id: String) = PaymentMethod(
            type = PaymentMethodType.Id,
            cardDetails = null,
            id,
            googlePayToken = null,
            billingAddress = null,
            options = null
        )

        fun googlePay(googlePayToken: String, billingAddress: Address?) = PaymentMethod(
            type = PaymentMethodType.GooglePay,
            cardDetails = null,
            id = null,
            googlePayToken,
            billingAddress,
            options = null
        )
    }
}
