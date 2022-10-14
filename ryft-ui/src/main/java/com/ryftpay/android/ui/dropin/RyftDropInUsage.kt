package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RyftDropInUsage : Parcelable {
    Payment,
    SetupCard
}
