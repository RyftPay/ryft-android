package com.ryftpay.android.ui.service

import com.google.android.gms.tasks.Task

internal interface GooglePayService {
    fun isReadyToPay(): Task<Boolean>
}
