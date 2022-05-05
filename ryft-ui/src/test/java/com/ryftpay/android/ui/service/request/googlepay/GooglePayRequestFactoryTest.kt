package com.ryftpay.android.ui.service.request.googlepay

import com.ryftpay.android.ui.TestData.merchantInfo
import com.ryftpay.android.ui.TestData.ryftTokenizationSpecification
import com.ryftpay.android.ui.TestData.transactionInfo
import com.ryftpay.android.ui.model.googlepay.LoadPaymentDataRequest
import com.ryftpay.android.ui.service.request.GooglePayRequestFactory
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class GooglePayRequestFactoryTest {

    private val isReadyToPayRequestJson = GooglePayRequestFactoryTest::class.java
        .getResource("/assets/googlepay/is-ready-to-pay-request.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    private val paymentDataRequestJson = GooglePayRequestFactoryTest::class.java
        .getResource("/assets/googlepay/payment-data-request.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "")

    @Test
    fun `isReadyToPayRequest returns expected request`() {
        GooglePayRequestFactory.isReadyToPayRequest.toJson() shouldBeEqualTo isReadyToPayRequestJson
    }

    @Test
    fun `createPaymentDataRequest returns expected request`() {
        GooglePayRequestFactory.createPaymentDataRequest(
            LoadPaymentDataRequest(
                merchantInfo,
                ryftTokenizationSpecification,
                transactionInfo
            )
        ).toJson() shouldBeEqualTo paymentDataRequestJson
    }
}
