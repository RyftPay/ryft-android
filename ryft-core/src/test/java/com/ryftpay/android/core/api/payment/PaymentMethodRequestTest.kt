package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.PAYMENT_METHOD_ID
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class PaymentMethodRequestTest {

    @Test
    fun `from should return request with id when provided`() {
        val request = PaymentMethodRequest.from(PAYMENT_METHOD_ID)
        request.id shouldBeEqualTo PAYMENT_METHOD_ID
    }
}
