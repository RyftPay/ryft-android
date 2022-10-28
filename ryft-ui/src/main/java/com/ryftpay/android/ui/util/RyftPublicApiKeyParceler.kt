package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import kotlinx.parcelize.Parceler

internal object RyftPublicApiKeyParceler : Parceler<RyftPublicApiKey> {
    override fun create(parcel: Parcel): RyftPublicApiKey = RyftPublicApiKey(
        value = parcel.readString()!!
    )

    override fun RyftPublicApiKey.write(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
    }
}
