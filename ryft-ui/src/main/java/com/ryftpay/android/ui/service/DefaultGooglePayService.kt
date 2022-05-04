package com.ryftpay.android.ui.service

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentsClient
import com.ryftpay.android.ui.service.request.googlepay.GooglePayRequestFactory
import com.ryftpay.android.ui.service.request.googlepay.LoadPaymentDataRequest

internal class DefaultGooglePayService(
    private val paymentsClient: PaymentsClient
) : GooglePayService {

    override fun isReadyToPay(): Task<Boolean> = paymentsClient.isReadyToPay(
        GooglePayRequestFactory.isReadyToPayRequest
    )

    override fun loadPaymentData(
        activity: Activity,
        loadPaymentDataRequest: LoadPaymentDataRequest
    ) {
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(
                GooglePayRequestFactory.createPaymentDataRequest(
                    loadPaymentDataRequest
                )
            ),
            activity,
            LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }

    companion object {
        const val LOAD_PAYMENT_DATA_REQUEST_CODE = 739
    }
}
