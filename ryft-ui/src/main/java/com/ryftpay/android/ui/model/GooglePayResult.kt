package com.ryftpay.android.ui.model

internal sealed class GooglePayResult {
    class Ok(val paymentData: GooglePayPaymentData) : GooglePayResult()

    object Failed : GooglePayResult()

    object Cancelled : GooglePayResult()
}
