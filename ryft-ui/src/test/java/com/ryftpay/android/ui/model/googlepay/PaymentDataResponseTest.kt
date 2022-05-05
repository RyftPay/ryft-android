package com.ryftpay.android.ui.model.googlepay

import com.google.android.gms.wallet.PaymentData
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class PaymentDataResponseTest {

    private val paymentDataResponse = PaymentDataResponseTest::class.java
        .getResource("/assets/googlepay/payment-data-response.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "") ?: ""

    @Test
    fun `from should return expected fields`() {
        val paymentDataResponse = PaymentDataResponse.from(
            PaymentData.fromJson(paymentDataResponse)
        )
        val expected = PaymentDataResponse(
            token = "examplePaymentMethodToken"
        )
        paymentDataResponse shouldBeEqualTo expected
    }
}
