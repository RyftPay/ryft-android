package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.api.payment.PaymentSessionResponse
import java.util.Currency

data class PaymentSession(
    val id: String,
    val amount: Int,
    val currency: Currency,
    val returnUrl: String,
    val status: PaymentSessionStatus,
    var customerEmail: String?,
    val lastError: PaymentSessionError?,
    val requiredAction: RequiredAction?,
    val createdTimestamp: Long,
    val lastUpdatedTimestamp: Long
) {

    companion object {
        internal fun from(response: PaymentSessionResponse): PaymentSession =
            PaymentSession(
                id = response.id,
                amount = response.amount,
                currency = Currency.getInstance(response.currency),
                returnUrl = response.returnUrl,
                status = PaymentSessionStatus.from(response.status),
                customerEmail = response.customerEmail,
                lastError = PaymentSessionError.from(response.lastError),
                requiredAction = response.requiredAction?.let { RequiredAction.from(it) },
                createdTimestamp = response.createdTimestamp,
                lastUpdatedTimestamp = response.lastUpdatedTimestamp
            )
    }
}
