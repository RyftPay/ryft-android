package com.ryftpay.android.ui.delegate

import android.view.View

internal interface RyftPaymentDelegate {
    fun onViewCreated(root: View, showGooglePay: Boolean)
}
