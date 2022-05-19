package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.address
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.Test

internal class BillingAddressRequestTest {

    @Test
    fun `from should return null when provided null`() {
        BillingAddressRequest.from(billingAddress = null) shouldBeEqualTo null
    }

    @Test
    fun `from should return all fields when address has all fields`() {
        val request = BillingAddressRequest.from(address)
        request shouldNotBeEqualTo null
        request!!.firstName shouldBeEqualTo address.firstName
        request.lastName shouldBeEqualTo address.lastName
        request.lineOne shouldBeEqualTo address.lineOne
        request.lineTwo shouldBeEqualTo address.lineTwo
        request.city shouldBeEqualTo address.city
        request.country shouldBeEqualTo address.country
        request.postalCode shouldBeEqualTo address.postalCode
        request.region shouldBeEqualTo address.region
    }

    @Test
    fun `from should return non null fields when address only has those fields set`() {
        val request = BillingAddressRequest.from(
            address.copy(
                firstName = null,
                lastName = null,
                lineOne = null,
                lineTwo = null,
                city = null,
                region = null
            )
        )
        request shouldNotBeEqualTo null
        request!!.firstName shouldBeEqualTo null
        request.lastName shouldBeEqualTo null
        request.lineOne shouldBeEqualTo null
        request.lineTwo shouldBeEqualTo null
        request.city shouldBeEqualTo null
        request.country shouldBeEqualTo address.country
        request.postalCode shouldBeEqualTo address.postalCode
        request.region shouldBeEqualTo null
    }
}
