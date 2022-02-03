package com.ryftpay.android.core.model.payment

data class CardDetails(
    val number: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cvc: String
)
