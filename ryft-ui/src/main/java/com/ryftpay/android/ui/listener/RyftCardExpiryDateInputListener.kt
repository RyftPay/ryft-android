package com.ryftpay.android.ui.listener

import com.ryftpay.android.ui.model.RyftCardExpiryDate
import com.ryftpay.android.ui.model.ValidationState

internal interface RyftCardExpiryDateInputListener {
    fun onCardExpiryDateChanged(
        expiryDate: RyftCardExpiryDate,
        validationState: ValidationState
    )
}
