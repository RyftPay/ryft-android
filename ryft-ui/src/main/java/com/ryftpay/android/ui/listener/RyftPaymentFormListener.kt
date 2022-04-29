package com.ryftpay.android.ui.listener

import com.ryftpay.android.ui.model.RyftCard

internal interface RyftPaymentFormListener {
    fun onCancel()
    fun onPayByCard(card: RyftCard)
    fun onPayByGooglePay()
}
