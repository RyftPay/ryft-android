package com.ryftpay.android.ui.dropin

import com.ryftpay.android.ui.TestData.CLIENT_SECRET
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftDropInConfigurationTest {

    @Test
    internal fun `display should be default when not provided`() {
        RyftDropInConfiguration(
            clientSecret = CLIENT_SECRET,
            subAccountId = null
        ).display shouldBeEqualTo RyftDropInDisplayConfiguration.Default
    }

    @Test
    internal fun `googlePayConfiguration should be null when not provided`() {
        RyftDropInConfiguration(
            clientSecret = CLIENT_SECRET,
            subAccountId = null
        ).googlePayConfiguration shouldBeEqualTo null
    }
}
