package com.ryftpay.android.core.service

import com.ryftpay.android.core.model.payment.PaymentMethod

interface RyftPaymentService {
    fun attemptPayment(
        clientSecret: String,
        paymentMethod: PaymentMethod,
        subAccountId: String?
    )

    fun loadPaymentSession(
        paymentSessionId: String,
        clientSecret: String,
        subAccountId: String?
    )
}
