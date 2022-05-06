package com.ryftpay.android.ui.service

import com.google.android.gms.wallet.PaymentsClient
import com.ryftpay.android.ui.TestData.merchantInfo
import com.ryftpay.android.ui.TestData.ryftTokenizationSpecification
import com.ryftpay.android.ui.TestData.transactionInfo
import com.ryftpay.android.ui.model.googlepay.LoadPaymentDataRequest
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class DefaultGooglePayServiceTest {

    private val isReadyToPayRequestJson = DefaultGooglePayServiceTest::class.java
        .getResource("/assets/googlepay/is-ready-to-pay-request.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    private val paymentDataRequestJson = DefaultGooglePayServiceTest::class.java
        .getResource("/assets/googlepay/payment-data-request.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    private val client = mockk<PaymentsClient>(relaxed = true)
    private val service: GooglePayService = DefaultGooglePayService(client)

    @Test
    fun `isReadyToPay uses expected request`() {
        service.isReadyToPay()

        verify {
            client.isReadyToPay(
                match {
                    it.toJson() == isReadyToPayRequestJson
                }
            )
        }
    }

    @Test
    fun `loadPaymentData uses expected request`() {
        service.loadPaymentData(
            mockk(relaxed = true),
            LoadPaymentDataRequest(
                merchantInfo,
                ryftTokenizationSpecification,
                transactionInfo
            )
        )

        verify {
            client.loadPaymentData(
                match {
                    it.toJson() == paymentDataRequestJson
                }
            )
        }
    }
}
