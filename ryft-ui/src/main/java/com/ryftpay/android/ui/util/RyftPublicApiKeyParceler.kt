package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import kotlinx.parcelize.Parceler
import java.lang.IllegalArgumentException

internal object RyftPublicApiKeyParceler : Parceler<RyftPublicApiKey> {
    override fun create(parcel: Parcel): RyftPublicApiKey {
        return RyftPublicApiKey(
            value = parcel.readString()
                ?: throw IllegalArgumentException("Invalid API key, string in parcel was null")
        )
    }

    override fun RyftPublicApiKey.write(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
    }
}
