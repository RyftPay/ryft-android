package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RyftPaymentResult : Parcelable {
    @Parcelize
    object Approved : RyftPaymentResult()

    @Parcelize
    class Failed(val error: RyftPaymentError) : RyftPaymentResult()

    @Parcelize
    object Cancelled : RyftPaymentResult()
}
