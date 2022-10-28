package com.ryftpay.android.ui.util

import android.os.Parcel
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionError
import com.ryftpay.android.core.model.payment.PaymentSessionStatus
import com.ryftpay.android.ui.util.RequiredActionParceler.write
import kotlinx.parcelize.Parceler
import java.util.Currency

internal object PaymentSessionParceler : Parceler<PaymentSession> {
    override fun create(parcel: Parcel): PaymentSession {
        val id = parcel.readString()!!
        val amount = parcel.readInt()
        val currency = Currency.getInstance(parcel.readString()!!)
        val returnUrl = parcel.readString()!!
        val status = PaymentSessionStatus.valueOf(parcel.readString()!!)
        val customerEmail = parcel.readString()
        val maybeLastError = parcel.readString()
        val lastError = if (maybeLastError != null) {
            PaymentSessionError.valueOf(maybeLastError)
        } else {
            null
        }
        val requiredAction = if (parcel.readInt() != 0) {
            RequiredActionParceler.create(parcel)
        } else {
            null
        }
        val createdTimestamp = parcel.readLong()
        val lastUpdatedTimestamp = parcel.readLong()
        return PaymentSession(
            id,
            amount,
            currency,
            returnUrl,
            status,
            customerEmail,
            lastError,
            requiredAction,
            createdTimestamp,
            lastUpdatedTimestamp
        )
    }

    override fun PaymentSession.write(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(amount)
        parcel.writeString(currency.currencyCode)
        parcel.writeString(returnUrl)
        parcel.writeString(status.toString())
        parcel.writeString(customerEmail)
        parcel.writeString(lastError?.toString())
        if (requiredAction == null) {
            parcel.writeInt(0)
        } else {
            parcel.writeInt(1)
            requiredAction!!.write(parcel, flags)
        }
        parcel.writeLong(createdTimestamp)
        parcel.writeLong(lastUpdatedTimestamp)
    }
}
