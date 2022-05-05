package com.ryftpay.android.ui.model.googlepay

internal sealed class GooglePayResult {
    class Ok(val paymentData: PaymentDataResponse) : GooglePayResult()

    object Failed : GooglePayResult()

    object Cancelled : GooglePayResult()
}
