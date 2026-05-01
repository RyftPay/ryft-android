package com.ryftpay.android.core.service

import com.ryftpay.android.core.model.payment.CustomerDetails
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.ThreeDsTransactionParams
import com.ryftpay.android.core.service.listener.RyftLoadPaymentListener
import com.ryftpay.android.core.service.listener.RyftPaymentResultListener

interface RyftPaymentService {
    fun attemptPayment(
        clientSecret: String,
        paymentMethod: PaymentMethod,
        customerDetails: CustomerDetails?,
        subAccountId: String?,
        listener: RyftPaymentResultListener
    )

    fun continuePayment(
        clientSecret: String,
        subAccountId: String?,
        threeDsTransactionParams: ThreeDsTransactionParams,
        listener: RyftPaymentResultListener
    )

    fun continuePaymentAfterChallenge(
        clientSecret: String,
        subAccountId: String?,
        transactionStatus: String,
        threeDSServerTransactionId: String,
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
