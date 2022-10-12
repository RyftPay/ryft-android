package com.ryftpay.android.core.model.payment

data class PaymentMethodOptions(
    val store: Boolean
) {
    companion object {
        fun card(store: Boolean): PaymentMethodOptions = PaymentMethodOptions(store)
    }
}
