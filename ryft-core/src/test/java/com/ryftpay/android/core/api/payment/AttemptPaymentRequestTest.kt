package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.CLIENT_SECRET
import com.ryftpay.android.core.TestData.cardPaymentMethod
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class AttemptPaymentRequestTest {

    @Test
    fun `from should return expected client secret`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            cardPaymentMethod
        ).clientSecret shouldBeEqualTo CLIENT_SECRET
    }

    @Test
    fun `from should add card details when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            cardPaymentMethod
        ).cardDetails shouldBeEqualTo CardDetailsRequest.from(cardPaymentMethod.cardDetails)
    }
}
