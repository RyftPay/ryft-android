package com.ryftpay.android.core.service

import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.service.listener.RyftLoadPaymentListener
import com.ryftpay.android.core.service.listener.RyftPaymentResultListener

interface RyftPaymentService {
    fun attemptPayment(
        clientSecret: String,
        paymentMethod: PaymentMethod,
        subAccountId: String?,
        listener: RyftPaymentResultListener
    )

    fun getLatestPaymentResult(
        paymentSessionId: String,
        clientSecret: String,
        subAccountId: String?,
        listener: RyftPaymentResultListener
    )

    fun loadPaymentSession(
        clientSecret: String,
        subAccountId: String?,
        listener: RyftLoadPaymentListener
    )
}
