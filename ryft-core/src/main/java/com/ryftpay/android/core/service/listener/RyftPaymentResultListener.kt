package com.ryftpay.android.core.service.listener

import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionError

interface RyftPaymentResultListener {
    fun onAttemptingPayment() { }
    fun onLoadingPaymentResult() { }
    fun onPaymentApproved(response: PaymentSession)
    fun onPaymentRequiresRedirect(returnUrl: String, redirectUrl: String)
    fun onPaymentHasError(lastError: PaymentSessionError)
    fun onErrorObtainingPaymentResult(error: RyftError?, throwable: Throwable?)
}
