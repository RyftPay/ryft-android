package com.ryftpay.android.ui.dropin

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftDropInDisplayConfigurationTest {

    @Test
    internal fun `payButtonTitle should be null when not provided`() {
        RyftDropInDisplayConfiguration(
            usage = RyftDropInUsage.Payment
        ).payButtonTitle shouldBeEqualTo null
    }

    @Test
    internal fun `Default should be Payment usage with no pay button title`() {
        RyftDropInDisplayConfiguration.Default shouldBeEqualTo RyftDropInDisplayConfiguration(
            usage = RyftDropInUsage.Payment,
            payButtonTitle = null
        )
    }
}
