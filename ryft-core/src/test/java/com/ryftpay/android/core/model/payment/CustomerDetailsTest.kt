package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.CUSTOMER_EMAIL
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

internal class CustomerDetailsTest {

    @Test
    fun `from should return null when email is null`() {
        CustomerDetails.from(customerEmail = null) shouldBeEqualTo null
    }

    @Test
    fun `from should return object with email when email is provided`() {
        val customerDetails = CustomerDetails.from(CUSTOMER_EMAIL)
        customerDetails shouldNotBeEqualTo null
        customerDetails!!.email shouldBeEqualTo CUSTOMER_EMAIL
    }
}
