package com.ryftpay.android.ui.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

internal fun <T : Parcelable?> Bundle.getParcelableArgs(name: String, clazz: Class<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(name, clazz)
    } else {
        // This is now deprecated but we have to use it for older android versions
        @Suppress("DEPRECATION")
        this.getParcelable<T>(name)
    }
