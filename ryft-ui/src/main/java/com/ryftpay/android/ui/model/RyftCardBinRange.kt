package com.ryftpay.android.ui.model

internal data class RyftCardBinRange(
    internal val min: Int,
    internal val max: Int
) {
    internal fun matchesCardNumber(sanitisedCardNumber: String): Boolean {
        if (!sanitisedCardNumber.any()) {
            return false
        }
        val minCardSegmentToBinCheck =
            sanitisedCardNumber.take(min.toString().length).toInt()
        val maxCardSegmentToBinCheck =
            sanitisedCardNumber.take(max.toString().length).toInt()
        return (minCardSegmentToBinCheck in min..max) ||
            (maxCardSegmentToBinCheck in min..max)
    }
}
