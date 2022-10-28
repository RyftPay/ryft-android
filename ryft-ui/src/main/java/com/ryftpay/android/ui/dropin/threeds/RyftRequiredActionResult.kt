package com.ryftpay.android.ui.dropin.threeds

import android.os.Parcelable
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.ui.dropin.RyftPaymentError
import com.ryftpay.android.ui.util.PaymentSessionParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

sealed class RyftRequiredActionResult : Parcelable {
    @Parcelize
    class Success(val paymentSession: @WriteWith<PaymentSessionParceler> PaymentSession) : RyftRequiredActionResult()

    @Parcelize
    class Error(val error: RyftPaymentError) : RyftRequiredActionResult()
}
