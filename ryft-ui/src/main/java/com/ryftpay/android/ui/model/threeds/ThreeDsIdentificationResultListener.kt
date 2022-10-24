package com.ryftpay.android.ui.model.threeds

internal interface ThreeDsIdentificationResultListener {
    fun onThreeDsIdentificationResult(
        result: ThreeDsIdentificationResult,
        paymentMethodId: String
    )
}
