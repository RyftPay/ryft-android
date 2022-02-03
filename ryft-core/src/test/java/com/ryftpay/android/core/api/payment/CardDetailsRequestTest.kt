package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.cardDetails
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class CardDetailsRequestTest {

    @Test
    fun `from should return expected fields`() {
        val request = CardDetailsRequest.from(cardDetails)
        request.number shouldBeEqualTo cardDetails.number
        request.expiryMonth shouldBeEqualTo cardDetails.expiryMonth
        request.expiryYear shouldBeEqualTo cardDetails.expiryYear
        request.cvc shouldBeEqualTo cardDetails.cvc
    }
}
