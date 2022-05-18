package com.ryftpay.android.core.model.payment

data class Address(
    val firstName: String?,
    val lastName: String?,
    val lineOne: String?,
    val lineTwo: String?,
    val city: String?,
    val country: String,
    val postalCode: String,
    val region: String?
)
