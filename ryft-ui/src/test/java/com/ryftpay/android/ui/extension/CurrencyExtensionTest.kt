package com.ryftpay.android.ui.extension

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Currency

@RunWith(JUnitParamsRunner::class)
internal class CurrencyExtensionTest {

    @Test
    @Parameters(method = "expectedDivisors")
    internal fun `divisor should return expected divisor for currency`(
        currencyCode: String,
        expectedDivisor: Double
    ) {
        Currency.getInstance(currencyCode).divisor() shouldBeEqualTo expectedDivisor
    }

    private fun expectedDivisors(): Array<Any> = arrayOf(
        arrayOf("GBP", 100.0),
        arrayOf("USD", 100.0),
        arrayOf("EUR", 100.0),
        arrayOf("AUD", 100.0),
        arrayOf("NZD", 100.0),
        arrayOf("JPY", 1.0),
        arrayOf("CNY", 100.0),
        arrayOf("CZK", 100.0),
        arrayOf("DKK", 100.0),
        arrayOf("ZAR", 100.0),
        arrayOf("HUF", 100.0),
        arrayOf("INR", 100.0),
        arrayOf("CHF", 100.0),
        arrayOf("AED", 100.0),
        arrayOf("BHD", 1000.0),
        arrayOf("VND", 1.0),
    )
}
