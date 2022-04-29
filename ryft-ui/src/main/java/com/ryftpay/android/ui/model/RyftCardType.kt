package com.ryftpay.android.ui.model

import com.ryftpay.ui.R

internal enum class RyftCardType(
    val displayName: String,
    val cardLengths: IntArray,
    val cvcLength: Int,
    val cardNumberFormatGaps: IntArray,
    val binRanges: List<RyftCardBinRange>,
    val iconDrawableId: Int,
    val availableForGooglePay: Boolean,
    val googlePayName: String
) {
    Visa(
        displayName = "Visa",
        cardLengths = intArrayOf(13, 16),
        cvcLength = 3,
        cardNumberFormatGaps = intArrayOf(4, 8, 12),
        binRanges = listOf(RyftCardBinRange(min = 4, max = 4)),
        iconDrawableId = R.drawable.ic_ryft_visa,
        availableForGooglePay = true,
        googlePayName = "VISA"
    ),
    Mastercard(
        displayName = "Mastercard",
        cardLengths = intArrayOf(16),
        cvcLength = 3,
        cardNumberFormatGaps = intArrayOf(4, 8, 12),
        binRanges = listOf(
            RyftCardBinRange(min = 2221, max = 2720),
            RyftCardBinRange(min = 51, max = 55)
        ),
        iconDrawableId = R.drawable.ic_ryft_mastercard,
        availableForGooglePay = true,
        googlePayName = "MASTERCARD"
    ),
    Amex(
        displayName = "American Express",
        cardLengths = intArrayOf(15),
        cvcLength = 4,
        cardNumberFormatGaps = intArrayOf(4, 10),
        binRanges = listOf(
            RyftCardBinRange(min = 34, max = 34),
            RyftCardBinRange(min = 37, max = 37)
        ),
        iconDrawableId = R.drawable.ic_ryft_amex,
        availableForGooglePay = false,
        googlePayName = "AMEX"
    ),
    Unknown(
        displayName = "Unknown",
        cardLengths = intArrayOf(19),
        cvcLength = 4,
        cardNumberFormatGaps = intArrayOf(),
        binRanges = listOf(),
        iconDrawableId = R.drawable.ic_ryft_unknown_card,
        availableForGooglePay = false,
        googlePayName = "INVALID"
    );

    internal val maxFormattedCardLength = cardLengths.maxOf { it } + cardNumberFormatGaps.count()

    companion object {
        internal fun fromCardNumber(sanitisedCardNumber: String): RyftCardType {
            val matchingCardTypes = values().filter { cardType ->
                cardType.binRanges.any { binRange -> binRange.matchesCardNumber(sanitisedCardNumber) }
            }
            return if (matchingCardTypes.size == 1) {
                matchingCardTypes.first()
            } else Unknown
        }

        internal fun getGooglePaySupportedTypeNames(): List<String> =
            values().filter { it.availableForGooglePay }.map { it.googlePayName }
    }
}
