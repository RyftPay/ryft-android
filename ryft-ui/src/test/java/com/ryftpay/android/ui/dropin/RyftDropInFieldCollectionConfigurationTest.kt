package com.ryftpay.android.ui.dropin

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftDropInFieldCollectionConfigurationTest {
    @Test
    internal fun `Default should not collect name on card`() {
        RyftDropInFieldCollectionConfiguration.Default shouldBeEqualTo RyftDropInFieldCollectionConfiguration(
            nameOnCard = false
        )
    }
}
