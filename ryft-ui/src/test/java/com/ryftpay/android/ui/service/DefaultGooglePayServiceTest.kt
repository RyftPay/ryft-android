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

    private val isReadyToPayRequestJsonWithBillingAddressRequired = DefaultGooglePayServiceTest::class.java
        .getResource("/assets/googlepay/is-ready-to-pay-request-billing-address-required.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    private val paymentDataRequestJson = DefaultGooglePayServiceTest::class.java
        .getResource("/assets/googlepay/payment-data-request.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    private val paymentDataRequestJsonWithBillingAddressRequired = DefaultGooglePayServiceTest::class.java
        .getResource("/assets/googlepay/payment-data-request-billing-address-required.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    private val client = mockk<PaymentsClient>(relaxed = true)
    private val service: GooglePayService = DefaultGooglePayService(client)

    @Test
    fun `isReadyToPay uses expected request when billing address is not required`() {
        service.isReadyToPay(billingAddressRequired = false)

        verify {
            client.isReadyToPay(
                match {
                    it.toJson() == isReadyToPayRequestJson
                }
            )
        }
    }

    @Test
    fun `isReadyToPay uses expected request when billing address is required`() {
        service.isReadyToPay(billingAddressRequired = true)

        verify {
            client.isReadyToPay(
                match {
                    it.toJson() == isReadyToPayRequestJsonWithBillingAddressRequired
                }
            )
        }
    }

    @Test
    fun `loadPaymentData uses expected request when billing address is not required`() {
        service.loadPaymentData(
            mockk(relaxed = true),
            LoadPaymentDataRequest(
                merchantInfo,
                billingAddressRequired = false,
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

    @Test
    fun `loadPaymentData uses expected request when billing address is required`() {
        service.loadPaymentData(
            mockk(relaxed = true),
            LoadPaymentDataRequest(
                merchantInfo,
                billingAddressRequired = true,
                ryftTokenizationSpecification,
                transactionInfo
            )
        )

        verify {
            client.loadPaymentData(
                match {
                    it.toJson() == paymentDataRequestJsonWithBillingAddressRequired
                }
            )
        }
    }
}
