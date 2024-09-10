package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import kotlinx.parcelize.Parceler

internal object NullableRyftPublicApiKeyParceler : Parceler<RyftPublicApiKey?> {
    override fun create(parcel: Parcel): RyftPublicApiKey? = parcel.readString()?.let {
        RyftPublicApiKey(
            value = it
        )
    }

    override fun RyftPublicApiKey?.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this?.value)
    }
}
