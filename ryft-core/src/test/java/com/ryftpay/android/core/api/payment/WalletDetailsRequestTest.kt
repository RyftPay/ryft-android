package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.GOOGLE_PAY_TOKEN
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class WalletDetailsRequestTest {

    @Test
    fun `from should return expected fields`() {
        val request = WalletDetailsRequest.from(GOOGLE_PAY_TOKEN)
        request.type shouldBeEqualTo "GooglePay"
        request.googlePayToken shouldBeEqualTo GOOGLE_PAY_TOKEN
    }
}
