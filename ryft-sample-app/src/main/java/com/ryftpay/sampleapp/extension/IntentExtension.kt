package com.ryftpay.sampleapp.extension

import android.content.Intent
import android.os.Build
import android.os.Parcelable

internal fun <T : Parcelable?> Intent.getParcelableArgs(name: String, clazz: Class<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(name, clazz)
    } else {
        // This is now deprecated but we have to use it for older android versions
        @Suppress("DEPRECATION")
        this.getParcelableExtra<T>(name)
    }
