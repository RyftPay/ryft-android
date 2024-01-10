package com.ryftpay.android.ui.listener

import com.ryftpay.android.ui.model.RyftCardName
import com.ryftpay.android.ui.model.ValidationState

internal interface RyftCardNameInputListener {
    fun onCardNameChanged(
        name: RyftCardName,
        validationState: ValidationState
    )
}
