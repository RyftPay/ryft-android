package com.ryftpay.android.ui.listener

internal interface RyftPaymentFormBodyListener {
    fun onCardReadyForPayment()
    fun onAwaitingCardDetails()
}
