package com.ryftpay.android.ui.model

import com.ryftpay.ui.R
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RyftCardTypeTest {

    @Test
    internal fun `Visa should return expected values`() {
        val visa = RyftCardType.Visa
        visa.displayName shouldBeEqualTo "Visa"
        visa.cardLengths shouldBeEqualTo intArrayOf(13, 16)
        visa.cvcLength shouldBeEqualTo 3
        visa.cardNumberFormatGaps shouldBeEqualTo intArrayOf(4, 8, 12)
        visa.binRanges shouldBeEqualTo listOf(RyftCardBinRange(min = 4, max = 4))
        visa.iconDrawableId shouldBeEqualTo R.drawable.ic_ryft_visa
        visa.maxFormattedCardLength shouldBeEqualTo 19
        visa.availableForGooglePay shouldBeEqualTo true
        visa.googlePayName shouldBeEqualTo "VISA"
    }

    @Test
    internal fun `Mastercard should return expected values`() {
        val mastercard = RyftCardType.Mastercard
        mastercard.displayName shouldBeEqualTo "Mastercard"
        mastercard.cardLengths shouldBeEqualTo intArrayOf(16)
        mastercard.cvcLength shouldBeEqualTo 3
        mastercard.cardNumberFormatGaps shouldBeEqualTo intArrayOf(4, 8, 12)
        mastercard.binRanges shouldBeEqualTo listOf(
            RyftCardBinRange(min = 2221, max = 2720),
            RyftCardBinRange(min = 51, max = 55)
        )
        mastercard.iconDrawableId shouldBeEqualTo R.drawable.ic_ryft_mastercard
        mastercard.maxFormattedCardLength shouldBeEqualTo 19
        mastercard.availableForGooglePay shouldBeEqualTo true
        mastercard.googlePayName shouldBeEqualTo "MASTERCARD"
    }

    @Test
    internal fun `Amex should return expected values`() {
        val amex = RyftCardType.Amex
        amex.displayName shouldBeEqualTo "American Express"
        amex.cardLengths shouldBeEqualTo intArrayOf(15)
        amex.cvcLength shouldBeEqualTo 4
        amex.cardNumberFormatGaps shouldBeEqualTo intArrayOf(4, 10)
        amex.binRanges shouldBeEqualTo listOf(
            RyftCardBinRange(min = 34, max = 34),
            RyftCardBinRange(min = 37, max = 37)
        )
        amex.iconDrawableId shouldBeEqualTo R.drawable.ic_ryft_amex
        amex.maxFormattedCardLength shouldBeEqualTo 17
        amex.availableForGooglePay shouldBeEqualTo false
        amex.googlePayName shouldBeEqualTo "AMEX"
    }

    @Test
    internal fun `Unknown should return expected values`() {
        val unknown = RyftCardType.Unknown
        unknown.displayName shouldBeEqualTo "Unknown"
        unknown.cardLengths shouldBeEqualTo intArrayOf(19)
        unknown.cvcLength shouldBeEqualTo 4
        unknown.cardNumberFormatGaps shouldBeEqualTo intArrayOf()
        unknown.binRanges shouldBeEqualTo listOf()
        unknown.iconDrawableId shouldBeEqualTo R.drawable.ic_ryft_unknown_card
        unknown.maxFormattedCardLength shouldBeEqualTo 19
        unknown.availableForGooglePay shouldBeEqualTo false
        unknown.googlePayName shouldBeEqualTo null
    }

    @Test
    @Parameters(method = "unknownCardNumbers")
    internal fun `fromCardNumber should return unknown when card number does not match any bin range`(
        unknownCardNumber: String
    ) {
        RyftCardType.fromCardNumber(unknownCardNumber) shouldBeEqualTo RyftCardType.Unknown
    }

    @Test
    @Parameters(method = "visaCardNumbers")
    internal fun `fromCardNumber should return Visa when card number matches visa bin ranges`(
        visaCardNumber: String
    ) {
        RyftCardType.fromCardNumber(visaCardNumber) shouldBeEqualTo RyftCardType.Visa
    }

    @Test
    @Parameters(method = "mastercardCardNumbers")
    internal fun `fromCardNumber should return Mastercard when card number matches mastercard bin ranges`(
        mastercardCardNumber: String
    ) {
        RyftCardType.fromCardNumber(mastercardCardNumber) shouldBeEqualTo RyftCardType.Mastercard
    }

    @Test
    @Parameters(method = "amexCardNumbers")
    internal fun `fromCardNumber should return Amex when card number matches amex bin ranges`(
        amexCardNumber: String
    ) {
        RyftCardType.fromCardNumber(amexCardNumber) shouldBeEqualTo RyftCardType.Amex
    }

    @Test
    internal fun `getGooglePaySupportedTypeNames should return visa and mastercard`() {
        RyftCardType.getGooglePaySupportedTypeNames() shouldBeEqualTo listOf("VISA", "MASTERCARD")
    }

    private fun unknownCardNumbers(): Array<String> = arrayOf(
        "1",
        "2",
        "3",
        "5",
        "6",
        "7",
        "8",
        "9",
        "1234",
        "1234999999999999",
        "7454",
        "7454000000000000",
        "6011",
        "6011111111111117",
        "3056",
        "3056930009020004",
        "3566",
        "3566002020360505",
        "6200",
        "6200000000000005"
    )

    private fun visaCardNumbers(): Array<String> = arrayOf(
        "4",
        "4929",
        "4929939187355598",
        "4485383550284604",
        "4532307841419094",
        "4716014929481859",
        "4539677496449015"
    )

    private fun mastercardCardNumbers(): Array<String> = arrayOf(
        "54",
        "51",
        "5582",
        "5454422955385717",
        "5582087594680466",
        "5485727655082288",
        "5523335560550243",
        "5128888281063960"
    )

    private fun amexCardNumbers(): Array<String> = arrayOf(
        "34",
        "3418",
        "37",
        "3716",
        "348570250878868",
        "341869994762900",
        "371040610543651",
        "341507151650399",
        "371673921387168"
    )
}
