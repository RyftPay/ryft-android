package com.ryftpay.android.ui.extension

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Currency

@RunWith(JUnitParamsRunner::class)
internal class IntExtensionTest {

    @Test
    @Parameters(method = "expectedFormattedStrings")
    internal fun `formatPriceWithoutCurrencySymbol should return expected formatted string`(
        amount: Int,
        currencyCode: String,
        expectedString: String
    ) {
        amount.formatPriceWithoutCurrencySymbol(
            Currency.getInstance(currencyCode)
        ) shouldBeEqualTo expectedString
    }

    private fun expectedFormattedStrings(): Array<Any> = arrayOf(
        arrayOf(0, "GBP", "0.00"),
        arrayOf(1, "GBP", "0.01"),
        arrayOf(9, "GBP", "0.09"),
        arrayOf(10, "GBP", "0.10"),
        arrayOf(13, "GBP", "0.13"),
        arrayOf(49, "GBP", "0.49"),
        arrayOf(70, "GBP", "0.70"),
        arrayOf(100, "GBP", "1.00"),
        arrayOf(190, "GBP", "1.90"),
        arrayOf(7000, "GBP", "70.00"),
        arrayOf(3200, "GBP", "32.00"),
        arrayOf(9280, "GBP", "92.80"),
        arrayOf(5935, "GBP", "59.35"),
        arrayOf(30000, "GBP", "300.00"),
        arrayOf(159850, "GBP", "1598.50"),
        arrayOf(220, "EUR", "2.20"),
        arrayOf(5893, "EUR", "58.93"),
        arrayOf(390, "USD", "3.90"),
        arrayOf(9034, "USD", "90.34"),
        arrayOf(0, "JPY", "0"),
        arrayOf(1, "JPY", "1"),
        arrayOf(5, "JPY", "5"),
        arrayOf(10, "JPY", "10"),
        arrayOf(73, "JPY", "73"),
        arrayOf(360, "JPY", "360"),
        arrayOf(9048, "JPY", "9048"),
        arrayOf(89820, "JPY", "89820"),
        arrayOf(0, "BHD", "0.000"),
        arrayOf(1, "BHD", "0.001"),
        arrayOf(5, "BHD", "0.005"),
        arrayOf(10, "BHD", "0.010"),
        arrayOf(14, "BHD", "0.014"),
        arrayOf(60, "BHD", "0.060"),
        arrayOf(100, "BHD", "0.100"),
        arrayOf(130, "BHD", "0.130"),
        arrayOf(578, "BHD", "0.578"),
        arrayOf(1540, "BHD", "1.540"),
        arrayOf(1301, "BHD", "1.301"),
        arrayOf(7800, "BHD", "7.800"),
        arrayOf(6000, "BHD", "6.000"),
        arrayOf(90313, "BHD", "90.313")
    )
}
