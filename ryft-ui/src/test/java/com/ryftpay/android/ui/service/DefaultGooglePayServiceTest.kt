package com.ryftpay.android.ui.service

import com.google.android.gms.wallet.PaymentsClient
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class DefaultGooglePayServiceTest {

    private val isReadyToPayRequestJson = DefaultGooglePayServiceTest::class.java
        .getResource("/assets/googlepay/is-ready-to-pay-request.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    private val client = mockk<PaymentsClient>(relaxed = true)
    private val service = DefaultGooglePayService(client)

    @Test
    fun `isReadyToPay sends expected request`() {
        service.isReadyToPay()

        verify {
            client.isReadyToPay(
                match {
                    it.toJson() == isReadyToPayRequestJson
                }
            )
        }
    }
}
