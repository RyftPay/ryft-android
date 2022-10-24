package com.ryftpay.android.ui.service

import com.checkout.threeds.Checkout3DSService
import com.ryftpay.android.ui.TestData.identifyAction
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResultListener
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class DefaultCheckoutThreeDsServiceTest {

    private val checkout3DSService = mockk<Checkout3DSService>(relaxed = true)
    private val threeDsIdentificationResultListener = mockk<ThreeDsIdentificationResultListener>(relaxed = true)
    private val service: ThreeDsService = DefaultCheckoutThreeDsService(checkout3DSService)

    @Test
    fun `handleIdentification calls authenticate on checkout service with expected args`() {
        service.handleIdentification(
            identifyAction,
            threeDsIdentificationResultListener
        )

        verify {
            checkout3DSService.authenticate(
                authenticationParameters = match {
                    it.sessionId == identifyAction.sessionId &&
                        it.sessionSecret == identifyAction.sessionSecret &&
                        it.scheme == identifyAction.scheme
                },
                callback = any()
            )
        }
    }
}
