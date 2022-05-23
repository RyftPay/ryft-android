package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.CUSTOMER_EMAIL
import com.ryftpay.android.core.TestData.paymentSessionResponse
import com.ryftpay.android.core.TestData.requiredActionResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.util.Currency

internal class PaymentSessionTest {

    @Test
    fun `from should return expected non-nullable fields`() {
        val paymentSession = PaymentSession.from(paymentSessionResponse)
        paymentSession.id shouldBeEqualTo paymentSessionResponse.id
        paymentSession.amount shouldBeEqualTo paymentSessionResponse.amount
        paymentSession.currency shouldBeEqualTo Currency.getInstance(paymentSessionResponse.currency)
        paymentSession.returnUrl shouldBeEqualTo paymentSessionResponse.returnUrl
        paymentSession.status shouldBeEqualTo PaymentSessionStatus.from(paymentSessionResponse.status)
        paymentSession.lastError shouldBeEqualTo PaymentSessionError.from(paymentSessionResponse.lastError)
        paymentSession.createdTimestamp shouldBeEqualTo paymentSessionResponse.createdTimestamp
        paymentSession.lastUpdatedTimestamp shouldBeEqualTo paymentSessionResponse.lastUpdatedTimestamp
    }

    @Test
    fun `from should not return customer email when it is null`() {
        PaymentSession.from(
            paymentSessionResponse.copy(customerEmail = null)
        ).customerEmail shouldBeEqualTo null
    }

    @Test
    fun `from should return customer email when it is present`() {
        PaymentSession.from(
            paymentSessionResponse.copy(customerEmail = CUSTOMER_EMAIL)
        ).customerEmail shouldBeEqualTo CUSTOMER_EMAIL
    }

    @Test
    fun `from should not return last error when it is null`() {
        PaymentSession.from(
            paymentSessionResponse.copy(lastError = null)
        ).lastError shouldBeEqualTo null
    }

    @Test
    fun `from should return last error when it is present`() {
        PaymentSession.from(
            paymentSessionResponse.copy(lastError = "invalid_card_number")
        ).lastError shouldBeEqualTo PaymentSessionError.InvalidCardNumber
    }

    @Test
    fun `from should not return required action when it is null`() {
        PaymentSession.from(
            paymentSessionResponse.copy(requiredAction = null)
        ).requiredAction shouldBeEqualTo null
    }

    @Test
    fun `from should return required action when it is present`() {
        PaymentSession.from(
            paymentSessionResponse.copy(requiredAction = requiredActionResponse)
        ).requiredAction shouldBeEqualTo RequiredAction.from(requiredActionResponse)
    }
}
