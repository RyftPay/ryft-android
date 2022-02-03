package com.ryftpay.android.core.model.payment

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class PaymentSessionStatusTest {

    @Test
    @Parameters(method = "inputStringsToExpectedStatuses")
    fun `from should return expected statuses`(input: String, expected: PaymentSessionStatus) {
        PaymentSessionStatus.from(input) shouldBeEqualTo expected
    }

    private fun inputStringsToExpectedStatuses(): Array<Any> = arrayOf(
        arrayOf("PendingPayment", PaymentSessionStatus.PendingPayment),
        arrayOf("PendingAction", PaymentSessionStatus.PendingAction),
        arrayOf("Approved", PaymentSessionStatus.Approved),
        arrayOf("Captured", PaymentSessionStatus.Captured),
        arrayOf("Unknown", PaymentSessionStatus.Unknown),
        arrayOf("Processing", PaymentSessionStatus.Unknown),
        arrayOf("Garbage", PaymentSessionStatus.Unknown)
    )
}
