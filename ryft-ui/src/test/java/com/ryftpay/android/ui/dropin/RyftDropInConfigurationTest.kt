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

    @Test
    internal fun `subAccountPayment should return expected clientSecret and subAccountId`() {
        val subAccountId = "ac_123"
        val subAccountPaymentConfig = RyftDropInConfiguration.subAccountPayment(
            clientSecret = CLIENT_SECRET,
            subAccountId = subAccountId
        )
        subAccountPaymentConfig.clientSecret shouldBeEqualTo CLIENT_SECRET
        subAccountPaymentConfig.subAccountId shouldBeEqualTo subAccountId
    }

    @Test
    internal fun `subAccountPayment should default display when not provided`() {
        RyftDropInConfiguration.subAccountPayment(
            clientSecret = CLIENT_SECRET,
            subAccountId = "ac_123"
        ).display shouldBeEqualTo RyftDropInDisplayConfiguration.Default
    }

    @Test
    internal fun `subAccountPayment should use null googlePayConfiguration when not provided`() {
        RyftDropInConfiguration.subAccountPayment(
            clientSecret = CLIENT_SECRET,
            subAccountId = "ac_123"
        ).googlePayConfiguration shouldBeEqualTo null
    }

    @Test
    internal fun `standardAccountPayment should return expected clientSecret with null subAccountId`() {
        val standardAccountPaymentConfig = RyftDropInConfiguration.standardAccountPayment(
            clientSecret = CLIENT_SECRET
        )
        standardAccountPaymentConfig.clientSecret shouldBeEqualTo CLIENT_SECRET
        standardAccountPaymentConfig.subAccountId shouldBeEqualTo null
    }

    @Test
    internal fun `standardAccountPayment should default display when not provided`() {
        RyftDropInConfiguration.standardAccountPayment(
            clientSecret = CLIENT_SECRET
        ).display shouldBeEqualTo RyftDropInDisplayConfiguration.Default
    }

    @Test
    internal fun `standardAccountPayment should use null googlePayConfiguration when not provided`() {
        RyftDropInConfiguration.standardAccountPayment(
            clientSecret = CLIENT_SECRET
        ).googlePayConfiguration shouldBeEqualTo null
    }
}
