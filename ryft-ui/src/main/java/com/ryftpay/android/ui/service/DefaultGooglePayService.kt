package com.ryftpay.android.ui.service

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.ryftpay.android.ui.model.googlepay.BaseApiRequest
import com.ryftpay.android.ui.model.googlepay.LoadPaymentDataRequest
import com.ryftpay.android.ui.model.googlepay.ReadyToPayRequest

internal class DefaultGooglePayService(
    private val paymentsClient: PaymentsClient
) : GooglePayService {

    private val baseApiV2Request = BaseApiRequest(
        majorApiVersion = 2,
        minorApiVersion = 0
    )

    override fun isReadyToPay(
        billingAddressRequired: Boolean
    ): Task<Boolean> = paymentsClient.isReadyToPay(
        IsReadyToPayRequest.fromJson(
            ReadyToPayRequest(
                billingAddressRequired = billingAddressRequired
            ).toApiV2RequestJson(baseApiV2Request).toString()
        )
    )

    override fun loadPaymentData(
        activity: Activity,
        loadPaymentDataRequest: LoadPaymentDataRequest
    ) {
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(
                PaymentDataRequest.fromJson(
                    loadPaymentDataRequest.toApiV2RequestJson(baseApiV2Request).toString()
                )
            ),
            activity,
            GooglePayService.LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }
}
