package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.payment.IdentifyAction
import kotlinx.parcelize.Parceler

internal object IdentifyActionParceler : Parceler<IdentifyAction> {
    override fun create(parcel: Parcel): IdentifyAction = IdentifyAction(
        scheme = parcel.readString()!!,
        paymentMethodId = parcel.readString()!!,
        protocolVersion = parcel.readString()!!,
        ravelinPublicKey = parcel.readString()!!
    )

    override fun IdentifyAction.write(parcel: Parcel, flags: Int) {
        parcel.writeString(scheme)
        parcel.writeString(paymentMethodId)
        parcel.writeString(protocolVersion)
        parcel.writeString(ravelinPublicKey)
    }
}
