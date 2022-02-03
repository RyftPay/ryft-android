package com.ryftpay.android.ui.listener

import com.ryftpay.android.ui.model.RyftCardNumber
import com.ryftpay.android.ui.model.ValidationState

internal interface RyftCardNumberInputListener {
    fun onCardNumberChanged(
        cardNumber: RyftCardNumber,
        validationState: ValidationState
    )
}
