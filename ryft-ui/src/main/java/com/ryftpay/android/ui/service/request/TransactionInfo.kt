package com.ryftpay.android.ui.service.request

import com.ryftpay.android.core.model.payment.PaymentSession
import java.util.Currency

internal data class TransactionInfo(
    val paymentSessionId: String,
    val paymentAmount: Int,
    val currency: Currency,
    val countryCode: String,
    val paymentAmountStatus: PaymentAmountStatus,
    val checkoutOption: CheckoutOption
) {

    internal enum class PaymentAmountStatus {
        NotCurrentlyKnown,
        Estimated,
        Final
    }

    internal enum class CheckoutOption {
        Default,
        CompleteImmediatePurchase
    }

    companion object {
        internal fun from(
            paymentSession: PaymentSession,
            countryCode: String,
        ) = TransactionInfo(
            paymentSession.id,
            paymentSession.amount,
            paymentSession.currency,
            countryCode,
            PaymentAmountStatus.Final,
            CheckoutOption.CompleteImmediatePurchase
        )
    }
}
