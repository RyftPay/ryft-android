package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyftDropInConfiguration(
    val clientSecret: String,
    val subAccountId: String?
) : Parcelable
