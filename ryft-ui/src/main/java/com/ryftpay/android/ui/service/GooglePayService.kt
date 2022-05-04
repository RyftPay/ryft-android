package com.ryftpay.android.ui.service

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.ryftpay.android.ui.service.request.googlepay.LoadPaymentDataRequest

internal interface GooglePayService {
    fun isReadyToPay(): Task<Boolean>
    fun loadPaymentData(
        activity: Activity,
        loadPaymentDataRequest: LoadPaymentDataRequest
    )

    companion object {
        const val LOAD_PAYMENT_DATA_REQUEST_CODE = 739
    }
}
