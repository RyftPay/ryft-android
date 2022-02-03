package com.ryftpay.android.core.model.payment

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RequiredActionTypeTest {

    @Test
    @Parameters(method = "inputStringsToExpectedTypes")
    fun `from should return expected statuses`(input: String, expected: RequiredActionType) {
        RequiredActionType.from(input) shouldBeEqualTo expected
    }

    private fun inputStringsToExpectedTypes(): Array<Any> = arrayOf(
        arrayOf("Redirect", RequiredActionType.Redirect),
        arrayOf("Unknown", RequiredActionType.Unknown),
        arrayOf("Processing", RequiredActionType.Unknown),
        arrayOf("Garbage", RequiredActionType.Unknown)
    )
}
