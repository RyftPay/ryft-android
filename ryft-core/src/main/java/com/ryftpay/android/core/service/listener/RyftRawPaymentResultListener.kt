package com.ryftpay.android.core.service.listener

import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionError

// TODO refactor these listeners in next major version upgrade
interface RyftRawPaymentResultListener : RyftPaymentResultListener {
    fun onRawPaymentResult(response: PaymentSession)
    override fun onPaymentApproved(response: PaymentSession) { }
    override fun onPaymentRequiresRedirect(
        returnUrl: String,
        redirectUrl: String
    ) { }
    override fun onPaymentRequiresIdentification(
        returnUrl: String,
        identifyAction: IdentifyAction
    ) { }
    override fun onPaymentHasError(lastError: PaymentSessionError) { }
}
