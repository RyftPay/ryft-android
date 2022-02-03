package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.cardDetails
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class PaymentMethodTest {

    @Test
    fun `card should return a payment method with card details`() {
        PaymentMethod.card(cardDetails).cardDetails shouldBeEqualTo cardDetails
    }
}
