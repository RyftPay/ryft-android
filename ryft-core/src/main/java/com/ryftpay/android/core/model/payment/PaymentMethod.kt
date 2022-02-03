package com.ryftpay.android.core.model.payment

data class PaymentMethod(
    val cardDetails: CardDetails
) {
    companion object {
        fun card(cardDetails: CardDetails) = PaymentMethod(
            cardDetails = cardDetails
        )
    }
}
