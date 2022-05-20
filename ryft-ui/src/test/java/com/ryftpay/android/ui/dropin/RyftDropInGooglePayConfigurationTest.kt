package com.ryftpay.android.ui.dropin

import com.ryftpay.android.ui.TestData.GB_COUNTRY_CODE
import com.ryftpay.android.ui.TestData.MERCHANT_NAME
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftDropInGooglePayConfigurationTest {

    @Test
    internal fun `billingAddressRequired should be true`() {
        RyftDropInGooglePayConfiguration(
            merchantName = MERCHANT_NAME,
            merchantCountryCode = GB_COUNTRY_CODE
        ).billingAddressRequired shouldBeEqualTo true
    }
}
