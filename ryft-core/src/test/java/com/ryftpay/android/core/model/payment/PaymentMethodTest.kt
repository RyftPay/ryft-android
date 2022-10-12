package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.GOOGLE_PAY_TOKEN
import com.ryftpay.android.core.TestData.address
import com.ryftpay.android.core.TestData.cardDetails
import com.ryftpay.android.core.TestData.paymentMethodOptions
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class PaymentMethodTest {

    @Test
    fun `card should return a payment method with card details`() {
        PaymentMethod.card(cardDetails, paymentMethodOptions).cardDetails shouldBeEqualTo cardDetails
    }

    @Test
    fun `card should return a payment method with type card`() {
        PaymentMethod.card(cardDetails, paymentMethodOptions).type shouldBeEqualTo PaymentMethodType.Card
    }

    @Test
    fun `card should return a payment method with no google pay token`() {
        PaymentMethod.card(cardDetails, paymentMethodOptions).googlePayToken shouldBeEqualTo null
    }

    @Test
    fun `card should return a payment method with no billing address`() {
        PaymentMethod.card(cardDetails, paymentMethodOptions).billingAddress shouldBeEqualTo null
    }

    @Test
    fun `card should return a payment method with payment method options`() {
        PaymentMethod.card(cardDetails, paymentMethodOptions).options shouldBeEqualTo paymentMethodOptions
    }

    @Test
    fun `googlePay should return a payment method with no card details`() {
        PaymentMethod.googlePay(
            GOOGLE_PAY_TOKEN,
            billingAddress = null
        ).cardDetails shouldBeEqualTo null
    }

    @Test
    fun `googlePay should return a payment method with type card`() {
        PaymentMethod.googlePay(
            GOOGLE_PAY_TOKEN,
            billingAddress = null
        ).type shouldBeEqualTo PaymentMethodType.GooglePay
    }

    @Test
    fun `googlePay should return a payment method with no google pay token`() {
        PaymentMethod.googlePay(
            GOOGLE_PAY_TOKEN,
            billingAddress = null
        ).googlePayToken shouldBeEqualTo GOOGLE_PAY_TOKEN
    }

    @Test
    fun `googlePay should return a payment method with no billing address when none provided`() {
        PaymentMethod.googlePay(
            GOOGLE_PAY_TOKEN,
            billingAddress = null
        ).billingAddress shouldBeEqualTo null
    }

    @Test
    fun `googlePay should return a payment method with billing address when one provided`() {
        PaymentMethod.googlePay(
            GOOGLE_PAY_TOKEN,
            billingAddress = address
        ).billingAddress shouldBeEqualTo address
    }

    @Test
    fun `googlePay should return a payment method with no payment method options`() {
        PaymentMethod.googlePay(
            GOOGLE_PAY_TOKEN,
            billingAddress = null
        ).options shouldBeEqualTo null
    }
}
