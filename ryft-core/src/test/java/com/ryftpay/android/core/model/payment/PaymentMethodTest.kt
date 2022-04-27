package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.GOOGLE_PAY_TOKEN
import com.ryftpay.android.core.TestData.cardDetails
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class PaymentMethodTest {

    @Test
    fun `card should return a payment method with card details`() {
        PaymentMethod.card(cardDetails).cardDetails shouldBeEqualTo cardDetails
    }

    @Test
    fun `card should return a payment method with type card`() {
        PaymentMethod.card(cardDetails).type shouldBeEqualTo PaymentMethodType.Card
    }

    @Test
    fun `card should return a payment method with no google pay token`() {
        PaymentMethod.card(cardDetails).googlePayToken shouldBeEqualTo null
    }

    @Test
    fun `googlePay should return a payment method with card details`() {
        PaymentMethod.googlePay(GOOGLE_PAY_TOKEN).cardDetails shouldBeEqualTo null
    }

    @Test
    fun `googlePay should return a payment method with type card`() {
        PaymentMethod.googlePay(GOOGLE_PAY_TOKEN).type shouldBeEqualTo PaymentMethodType.GooglePay
    }

    @Test
    fun `googlePay should return a payment method with no google pay token`() {
        PaymentMethod.googlePay(GOOGLE_PAY_TOKEN).googlePayToken shouldBeEqualTo GOOGLE_PAY_TOKEN
    }
}
