package com.ryftpay.android.ui.listener

import com.ryftpay.android.ui.model.RyftCard
import com.ryftpay.android.ui.model.RyftCardOptions

internal interface RyftPaymentFormListener {
    fun onCancel()
    fun onPayByCard(card: RyftCard, cardOptions: RyftCardOptions)
    fun onPayByGooglePay()
}
