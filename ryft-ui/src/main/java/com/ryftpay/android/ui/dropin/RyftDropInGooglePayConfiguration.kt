package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyftDropInGooglePayConfiguration(
    val merchantName: String,
    val merchantCountryCode: String
) : Parcelable {
    @IgnoredOnParcel
    internal val existingPaymentMethodRequired = false
    @IgnoredOnParcel
    internal val billingAddressRequired = true
}
