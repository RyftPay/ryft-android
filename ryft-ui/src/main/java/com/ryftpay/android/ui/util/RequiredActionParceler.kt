package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.payment.RequiredAction
import com.ryftpay.android.core.model.payment.RequiredActionType
import com.ryftpay.android.ui.util.IdentifyActionParceler.write
import kotlinx.parcelize.Parceler

internal object RequiredActionParceler : Parceler<RequiredAction> {
    override fun create(parcel: Parcel): RequiredAction = RequiredAction(
        type = RequiredActionType.valueOf(parcel.readString()!!),
        url = parcel.readString(),
        identify = if (parcel.readInt() != 0) {
            IdentifyActionParceler.create(parcel)
        } else {
            null
        }
    )

    override fun RequiredAction.write(parcel: Parcel, flags: Int) {
        parcel.writeString(type.toString())
        parcel.writeString(url)
        if (identify == null) {
            parcel.writeInt(0)
        } else {
            parcel.writeInt(1)
            identify!!.write(parcel, flags)
        }
    }
}
