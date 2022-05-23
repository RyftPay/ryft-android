package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.customerDetails
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

internal class CustomerDetailsRequestTest {

    @Test
    fun `from should return null when provided null`() {
        CustomerDetailsRequest.from(customerDetails = null) shouldBeEqualTo null
    }

    @Test
    fun `from should return request with email when provided`() {
        val request = CustomerDetailsRequest.from(customerDetails)
        request shouldNotBeEqualTo null
        request!!.email shouldBeEqualTo customerDetails.email
    }
}
