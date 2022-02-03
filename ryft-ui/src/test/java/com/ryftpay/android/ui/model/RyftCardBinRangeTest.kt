package com.ryftpay.android.ui.model

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RyftCardBinRangeTest {

    @Test
    @Parameters(method = "invalidSanitisedCardNumbers")
    internal fun `matchesCardNumber should return false if sanitised card number is invalid`(
        invalidCardNumber: String
    ) {
        RyftCardBinRange(min = 2000, max = 2999).matchesCardNumber(invalidCardNumber) shouldBeEqualTo false
    }

    @Test
    @Parameters(method = "notMatchingBinRanges")
    internal fun `matchesCardNumber should return false if sanitised card number does not match bin range`(
        binRange: RyftCardBinRange,
        cardNumber: String
    ) {
        binRange.matchesCardNumber(cardNumber) shouldBeEqualTo false
    }

    @Test
    @Parameters(method = "matchingBinRanges")
    internal fun `matchesCardNumber should return true if sanitised card number does match bin range`(
        binRange: RyftCardBinRange,
        cardNumber: String
    ) {
        binRange.matchesCardNumber(cardNumber) shouldBeEqualTo true
    }

    private fun invalidSanitisedCardNumbers(): Array<String> = arrayOf(
        ""
    )

    private fun notMatchingBinRanges(): Array<Any> = arrayOf(
        arrayOf(RyftCardBinRange(min = 4, max = 4), "3"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "5"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "3999999999999999"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "5000000000000000"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "2"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "3"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "4"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "33"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "35"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "3399999999999999"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "3500000000000000"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "40"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "4"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "50"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "5"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "56"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "60"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "6"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "5099999999999999"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "5600000000000000"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "22"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "222"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2220999999999999"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "27"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "272"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2721000000000000")
    )

    private fun matchingBinRanges(): Array<Any> = arrayOf(
        arrayOf(RyftCardBinRange(min = 4, max = 4), "4"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "4000000000000000"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "41"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "4983"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "4999999999999999"),
        arrayOf(RyftCardBinRange(min = 4, max = 4), "4982347789239490"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "34"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "3400000000000000"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "3499999999999999"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "341"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "34898"),
        arrayOf(RyftCardBinRange(min = 34, max = 34), "3498928349729347"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "51"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "53"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "55"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "5100000000000000"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "5599999999999999"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "5234"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "55523"),
        arrayOf(RyftCardBinRange(min = 51, max = 55), "5209829384092830"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2221"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2720"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2293"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2589"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2221000000000000"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2720999999999999"),
        arrayOf(RyftCardBinRange(min = 2221, max = 2720), "2432098230948023")
    )
}
