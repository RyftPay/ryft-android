package com.ryftpay.android.core.extension

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class StringExtensionTest {

    @Test
    @Parameters(method = "extractPaymentSessionIdFromClientSecret")
    internal fun `extractPaymentSessionIdFromClientSecret should extract payment session id if present`(
        input: String,
        expected: String
    ) {
        input.extractPaymentSessionIdFromClientSecret() shouldBeEqualTo expected
    }

    private fun extractPaymentSessionIdFromClientSecret(): Array<Any> = arrayOf(
        arrayOf("", ""),
        arrayOf("test", "test"),
        arrayOf("ps_01J84NH4ZD6VPCTY6MJJ8HGBWC", "ps_01J84NH4ZD6VPCTY6MJJ8HGBWC"),
        arrayOf("ps_01J84NH4ZD6VPCTY6MJJ8HGBWC_secret_f787939a-a25b-45ba-b1c4-df57f1dfa49e", "ps_01J84NH4ZD6VPCTY6MJJ8HGBWC"),
        arrayOf("ps_123_secret_456", "ps_123"),
        arrayOf("ps_01J84NC8C0E3888G2S9K8K1WBV_secret_58e1e731-f880-440b-a589-b2c7c570a2bc", "ps_01J84NC8C0E3888G2S9K8K1WBV")
    )
}
