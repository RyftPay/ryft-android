package com.ryftpay.android.core.model.payment

data class ThreeDsTransactionParams(
    val sdkTransactionId: String,
    val sdkApplicationId: String,
    val sdkEncryptedData: String,
    val sdkEphemeralPublicKey: String,
    val sdkReferenceNumber: String
)
