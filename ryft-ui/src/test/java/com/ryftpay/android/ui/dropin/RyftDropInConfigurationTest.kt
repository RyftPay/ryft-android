package com.ryftpay.android.ui.dropin

import com.ryftpay.android.ui.TestData.CLIENT_SECRET
import com.ryftpay.android.ui.TestData.GB_COUNTRY_CODE
import com.ryftpay.android.ui.TestData.MERCHANT_NAME
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftDropInConfigurationTest {

    @Test
    internal fun `googlePayConfiguration should be null when not provided`() {
        RyftDropInConfiguration(
            clientSecret = CLIENT_SECRET,
            subAccountId = null
        ).googlePayConfiguration shouldBeEqualTo null
    }

    @Test
    internal fun `googlePayEnabled should return true when google pay configuration is not null`() {
        RyftDropInConfiguration(
            clientSecret = CLIENT_SECRET,
            subAccountId = null,
            googlePayConfiguration = RyftDropInGooglePayConfiguration(
                merchantName = MERCHANT_NAME,
                merchantCountryCode = GB_COUNTRY_CODE
            )
        ).googlePayEnabled shouldBeEqualTo true
    }

    @Test
    internal fun `googlePayEnabled should return false when google pay configuration is null`() {
        RyftDropInConfiguration(
            clientSecret = CLIENT_SECRET,
            subAccountId = null,
            googlePayConfiguration = null
        ).googlePayEnabled shouldBeEqualTo false
    }
}
