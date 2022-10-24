package com.ryftpay.android.ui.model.threeds

import com.checkout.threeds.domain.model.AuthenticationResult
import com.checkout.threeds.domain.model.ResultType

internal sealed class ThreeDsIdentificationResult {
    object Success : ThreeDsIdentificationResult()
    object Fail : ThreeDsIdentificationResult()
    object Error : ThreeDsIdentificationResult()

    companion object {
        internal fun fromCheckoutResult(
            authenticationResult: AuthenticationResult
        ): ThreeDsIdentificationResult = when (authenticationResult.resultType) {
            ResultType.Successful -> Success
            ResultType.Failed -> Fail
            ResultType.Error -> Error
        }
    }
}
