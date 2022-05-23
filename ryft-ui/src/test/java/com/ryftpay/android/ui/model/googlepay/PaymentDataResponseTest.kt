package com.ryftpay.android.ui.model.googlepay

import com.google.android.gms.wallet.PaymentData
import com.ryftpay.android.core.model.payment.Address
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class PaymentDataResponseTest {

    private val paymentDataResponse = PaymentDataResponseTest::class.java
        .getResource("/assets/googlepay/payment-data-response.json")
        ?.readText() ?: ""

    private val paymentDataResponseWithFullBillingAddress = PaymentDataResponseTest::class.java
        .getResource("/assets/googlepay/payment-data-response-with-full-billing-address.json")
        ?.readText() ?: ""

    private val paymentDataResponseWithPartialBillingAddress = PaymentDataResponseTest::class.java
        .getResource("/assets/googlepay/payment-data-response-with-partial-billing-address.json")
        ?.readText() ?: ""

    private val paymentDataResponseWithEmail = PaymentDataResponseTest::class.java
        .getResource("/assets/googlepay/payment-data-response-with-email.json")
        ?.readText() ?: ""

    @Test
    fun `from should return expected fields when no billing address or email is present`() {
        val paymentDataResponse = PaymentDataResponse.from(
            PaymentData.fromJson(paymentDataResponse)
        )
        val expected = PaymentDataResponse(
            token = "examplePaymentMethodToken",
            billingAddress = null,
            email = null
        )
        paymentDataResponse shouldBeEqualTo expected
    }

    @Test
    fun `from should return expected fields when a full billing address is present`() {
        val paymentDataResponse = PaymentDataResponse.from(
            PaymentData.fromJson(paymentDataResponseWithFullBillingAddress)
        )
        val expected = PaymentDataResponse(
            token = "examplePaymentMethodToken",
            billingAddress = Address(
                firstName = "John",
                lastName = "Doe",
                lineOne = "c/o Google LLC",
                lineTwo = "1600 Amphitheatre Pkwy",
                city = "Mountain View",
                country = "US",
                postalCode = "94043",
                region = "CA"
            ),
            email = null
        )
        paymentDataResponse shouldBeEqualTo expected
    }

    @Test
    fun `from should return expected billing address when optional fields are null or empty`() {
        val paymentDataResponse = PaymentDataResponse.from(
            PaymentData.fromJson(paymentDataResponseWithPartialBillingAddress)
        )
        val expected = PaymentDataResponse(
            token = "examplePaymentMethodToken",
            billingAddress = Address(
                firstName = null,
                lastName = null,
                lineOne = null,
                lineTwo = null,
                city = null,
                country = "GB",
                postalCode = "SW1A 2AA",
                region = null
            ),
            email = null
        )
        paymentDataResponse shouldBeEqualTo expected
    }

    @Test
    fun `from should return expected fields when email is present`() {
        val paymentDataResponse = PaymentDataResponse.from(
            PaymentData.fromJson(paymentDataResponseWithEmail)
        )
        val expected = PaymentDataResponse(
            token = "examplePaymentMethodToken",
            billingAddress = null,
            email = "test@example.com"
        )
        paymentDataResponse shouldBeEqualTo expected
    }
}
