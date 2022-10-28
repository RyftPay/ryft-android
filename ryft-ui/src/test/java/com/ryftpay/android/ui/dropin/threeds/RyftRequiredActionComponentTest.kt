package com.ryftpay.android.ui.dropin.threeds

import com.ryftpay.android.ui.TestData.CLIENT_SECRET
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftRequiredActionComponentTest {

    @Test
    internal fun `subAccountPayment should return expected clientSecret and subAccountId`() {
        val subAccountId = "ac_123"
        val subAccountPaymentConfig = RyftRequiredActionComponent.Configuration.subAccountPayment(
            clientSecret = CLIENT_SECRET,
            subAccountId = subAccountId
        )
        subAccountPaymentConfig.clientSecret shouldBeEqualTo CLIENT_SECRET
        subAccountPaymentConfig.subAccountId shouldBeEqualTo subAccountId
    }

    @Test
    internal fun `subAccountPayment should use null returnUrl when not provided`() {
        RyftRequiredActionComponent.Configuration.subAccountPayment(
            clientSecret = CLIENT_SECRET,
            subAccountId = "ac_123"
        ).returnUrl shouldBeEqualTo null
    }

    @Test
    internal fun `standardAccountPayment should return expected clientSecret with null subAccountId`() {
        val standardAccountPaymentConfig = RyftRequiredActionComponent.Configuration.standardAccountPayment(
            clientSecret = CLIENT_SECRET
        )
        standardAccountPaymentConfig.clientSecret shouldBeEqualTo CLIENT_SECRET
        standardAccountPaymentConfig.subAccountId shouldBeEqualTo null
    }

    @Test
    internal fun `standardAccountPayment should use null returnUrl when not provided`() {
        RyftRequiredActionComponent.Configuration.standardAccountPayment(
            clientSecret = CLIENT_SECRET
        ).returnUrl shouldBeEqualTo null
    }
}
