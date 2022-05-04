package com.ryftpay.android.ui.model

import com.google.android.gms.wallet.PaymentData
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class GooglePayPaymentDataTest {

    private val paymentDataResponse = GooglePayPaymentDataTest::class.java
        .getResource("/assets/googlepay/payment-data-response.json")
        ?.readText()
        ?.replace(Regex("\\s+"), "") ?: ""

    @Test
    fun `from should return expected fields`() {
        val googlePayPaymentData = GooglePayPaymentData.from(
            PaymentData.fromJson(paymentDataResponse)
        )
        val expected = GooglePayPaymentData(
            token = "examplePaymentMethodToken"
        )
        googlePayPaymentData shouldBeEqualTo expected
    }
}
