package com.ryftpay.android.ui.model

import com.ryftpay.ui.R

internal enum class RyftCardType(
    val displayName: String,
    val cardLengths: IntArray,
    val cvcLength: Int,
    val cardNumberFormatGaps: IntArray,
    val binRanges: List<RyftCardBinRange>,
    val iconDrawableId: Int
) {
    Visa(
        displayName = "Visa",
        cardLengths = intArrayOf(13, 16),
        cvcLength = 3,
        cardNumberFormatGaps = intArrayOf(4, 8, 12),
        binRanges = listOf(RyftCardBinRange(min = 4, max = 4)),
        iconDrawableId = R.drawable.ic_ryft_visa
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
        iconDrawableId = R.drawable.ic_ryft_mastercard
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
        iconDrawableId = R.drawable.ic_ryft_amex
    ),
    Unknown(
        displayName = "Unknown",
        cardLengths = intArrayOf(19),
        cvcLength = 4,
        cardNumberFormatGaps = intArrayOf(),
        binRanges = listOf(),
        iconDrawableId = R.drawable.ic_ryft_unknown_card
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
    }
}
