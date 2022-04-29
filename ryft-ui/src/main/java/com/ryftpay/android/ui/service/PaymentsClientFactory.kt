package com.ryftpay.android.ui.service

import android.app.Activity
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.ryftpay.android.core.model.api.RyftEnvironment
import com.ryftpay.android.ui.extension.toGooglePayEnvironment

internal object PaymentsClientFactory {

    fun createPaymentsClient(
        ryftEnvironment: RyftEnvironment,
        activity: Activity
    ): PaymentsClient = Wallet.getPaymentsClient(
        activity,
        Wallet.WalletOptions.Builder()
            .setEnvironment(ryftEnvironment.toGooglePayEnvironment())
            .build()
    )
}
