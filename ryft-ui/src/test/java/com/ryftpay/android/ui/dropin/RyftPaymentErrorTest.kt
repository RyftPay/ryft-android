package com.ryftpay.android.ui.dropin

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftPaymentErrorTest {

    @Test
    internal fun `Unexpected should return expected error`() {
        val error = RyftPaymentError.Unexpected
        error.paymentSessionError shouldBeEqualTo null
        error.displayError shouldBeEqualTo "An unexpected error occurred"
    }
}
