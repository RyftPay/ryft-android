package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.cardDetails
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class CardDetailsRequestTest {

    @Test
    fun `from should return expected fields when nullable fields are present`() {
        val request = CardDetailsRequest.from(cardDetails.copy(name = "present"))
        request.number shouldBeEqualTo cardDetails.number
        request.expiryMonth shouldBeEqualTo cardDetails.expiryMonth
        request.expiryYear shouldBeEqualTo cardDetails.expiryYear
        request.cvc shouldBeEqualTo cardDetails.cvc
        request.name shouldBeEqualTo "present"
    }

    @Test
    fun `from should return expected fields when nullable fields are null`() {
        val request = CardDetailsRequest.from(cardDetails.copy(name = null))
        request.number shouldBeEqualTo cardDetails.number
        request.expiryMonth shouldBeEqualTo cardDetails.expiryMonth
        request.expiryYear shouldBeEqualTo cardDetails.expiryYear
        request.cvc shouldBeEqualTo cardDetails.cvc
        request.name shouldBeEqualTo null
    }
}
