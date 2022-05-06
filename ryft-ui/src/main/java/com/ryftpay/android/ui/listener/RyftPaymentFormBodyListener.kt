package com.ryftpay.android.ui.listener

internal interface RyftPaymentFormBodyListener {
    fun onReadyForCardPayment()
    fun onAwaitingCardDetails()
}
