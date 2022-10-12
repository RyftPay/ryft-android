package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.paymentMethodOptions
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

internal class PaymentMethodOptionsRequestTest {

    @Test
    fun `from should return null when options are null`() {
        val request = PaymentMethodOptionsRequest.from(options = null)
        request shouldBeEqualTo null
    }

    @Test
    fun `from should set store to false when store on options is set to false`() {
        val request = PaymentMethodOptionsRequest.from(paymentMethodOptions.copy(store = false))
        request shouldNotBeEqualTo null
        request!!.store shouldBeEqualTo false
    }

    @Test
    fun `from should set store to true when store on options is set to true`() {
        val request = PaymentMethodOptionsRequest.from(paymentMethodOptions.copy(store = true))
        request shouldNotBeEqualTo null
        request!!.store shouldBeEqualTo true
    }
}
