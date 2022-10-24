package com.ryftpay.android.ui.service

import com.checkout.threeds.Checkout3DSService
import com.checkout.threeds.domain.model.AuthenticationParameters
import com.checkout.threeds.domain.model.AuthenticationResult
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResult
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResultListener

internal class DefaultCheckoutThreeDsService(
    private val checkout3DSService: Checkout3DSService
) : ThreeDsService {

    override fun handleIdentification(
        identifyAction: IdentifyAction,
        listener: ThreeDsIdentificationResultListener
    ) {
        checkout3DSService.authenticate(
            authenticationParameters = AuthenticationParameters(
                identifyAction.sessionId,
                identifyAction.sessionSecret,
                identifyAction.scheme
            )
        ) { authenticationResult: AuthenticationResult ->
            listener.onThreeDsIdentificationResult(
                ThreeDsIdentificationResult.fromCheckoutResult(authenticationResult),
                identifyAction.paymentMethodId
            )
        }
    }
}
