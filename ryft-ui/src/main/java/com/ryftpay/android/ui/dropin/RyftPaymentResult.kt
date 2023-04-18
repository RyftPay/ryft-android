package com.ryftpay.android.ui.dropin

import android.os.Parcelable
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.ui.util.PaymentSessionParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

sealed class RyftPaymentResult : Parcelable {
    @Parcelize
    class Approved(val paymentSession: @WriteWith<PaymentSessionParceler> PaymentSession) : RyftPaymentResult()

    @Parcelize
    class Failed(val error: RyftPaymentError) : RyftPaymentResult()

    @Parcelize
    object Cancelled : RyftPaymentResult()
}
