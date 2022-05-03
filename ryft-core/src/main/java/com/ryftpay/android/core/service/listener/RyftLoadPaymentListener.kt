package com.ryftpay.android.core.service.listener

import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.PaymentSession

interface RyftLoadPaymentListener {
    fun onLoadingPayment() { }
    fun onErrorLoadingPayment(error: RyftError?, throwable: Throwable?)
    fun onPaymentLoaded(response: PaymentSession)
}
