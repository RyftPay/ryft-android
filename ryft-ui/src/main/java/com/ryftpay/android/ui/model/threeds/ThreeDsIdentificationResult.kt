package com.ryftpay.android.ui.model.threeds

import com.checkout.threeds.domain.model.AuthenticationCompleted
import com.checkout.threeds.domain.model.AuthenticationResult

internal sealed class ThreeDsIdentificationResult {
    object Success : ThreeDsIdentificationResult()
    object Fail : ThreeDsIdentificationResult()
    object Error : ThreeDsIdentificationResult()

    companion object {
        private val SuccessfulStatuses = setOf(
            "Y",
            "I",
            "A"
        )
        internal fun fromCheckoutResult(
            authenticationResult: AuthenticationResult
        ): ThreeDsIdentificationResult = when (authenticationResult) {
            is AuthenticationCompleted -> when (authenticationResult.transactionStatus) {
                in SuccessfulStatuses -> Success
                else -> Fail
            }
            else -> Error
        }
    }
}
