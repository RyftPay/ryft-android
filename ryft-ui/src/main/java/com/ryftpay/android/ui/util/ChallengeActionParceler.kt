package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.payment.ChallengeAction
import kotlinx.parcelize.Parceler

internal object ChallengeActionParceler : Parceler<ChallengeAction> {
    override fun create(parcel: Parcel): ChallengeAction = ChallengeAction(
        threeDSServerTransactionId = parcel.readString()!!,
        acsTransactionId = parcel.readString()!!,
        acsRefNumber = parcel.readString()!!,
        acsSignedContent = parcel.readString()!!
    )

    override fun ChallengeAction.write(parcel: Parcel, flags: Int) {
        parcel.writeString(threeDSServerTransactionId)
        parcel.writeString(acsTransactionId)
        parcel.writeString(acsRefNumber)
        parcel.writeString(acsSignedContent)
    }
}
