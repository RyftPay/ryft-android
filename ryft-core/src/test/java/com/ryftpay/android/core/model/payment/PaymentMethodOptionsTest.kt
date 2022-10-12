package com.ryftpay.android.core.model.payment

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class PaymentMethodOptionsTest {

    @Test
    fun `card should return options with store set to false when it is`() {
        PaymentMethodOptions.card(store = false).store shouldBeEqualTo false
    }

    @Test
    fun `card should return options with store set to true when it is`() {
        PaymentMethodOptions.card(store = true).store shouldBeEqualTo true
    }
}
