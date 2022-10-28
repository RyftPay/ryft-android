package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.payment.IdentifyAction
import kotlinx.parcelize.Parceler

internal object IdentifyActionParceler : Parceler<IdentifyAction> {
    override fun create(parcel: Parcel): IdentifyAction = IdentifyAction(
        sessionId = parcel.readString()!!,
        sessionSecret = parcel.readString()!!,
        scheme = parcel.readString()!!,
        paymentMethodId = parcel.readString()!!
    )

    override fun IdentifyAction.write(parcel: Parcel, flags: Int) {
        parcel.writeString(sessionId)
        parcel.writeString(sessionSecret)
        parcel.writeString(scheme)
        parcel.writeString(paymentMethodId)
    }
}
