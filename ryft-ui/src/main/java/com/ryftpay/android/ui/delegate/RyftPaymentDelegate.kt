package com.ryftpay.android.ui.delegate

import android.view.View
import com.ryftpay.android.ui.dropin.RyftDropInUsage

internal interface RyftPaymentDelegate {
    fun onViewCreated(
        root: View,
        usage: RyftDropInUsage,
        payButtonTitleOverride: String?,
        googlePayAvailable: Boolean,
        collectNameOnCard: Boolean
    )
    fun onGooglePayPaymentProcessing()
    fun onGooglePayFailedOrCancelled()
}
