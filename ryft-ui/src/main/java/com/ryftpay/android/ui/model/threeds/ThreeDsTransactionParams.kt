package com.ryftpay.android.ui.model.threeds

internal data class ThreeDsTransactionParams(
    val sdkTransactionId: String,
    val sdkApplicationId: String,
    val sdkEncryptedData: String,
    val sdkEphemeralPublicKey: String,
    val sdkReferenceNumber: String
)
