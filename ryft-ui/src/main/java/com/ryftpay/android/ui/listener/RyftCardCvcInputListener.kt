package com.ryftpay.android.ui.listener

import com.ryftpay.android.ui.model.RyftCardCvc

internal interface RyftCardCvcInputListener {
    fun onCardCvcChanged(cvc: RyftCardCvc)
}
