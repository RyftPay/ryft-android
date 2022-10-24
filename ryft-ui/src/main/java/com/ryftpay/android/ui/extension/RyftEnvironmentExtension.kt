package com.ryftpay.android.ui.extension

import com.checkout.threeds.Environment
import com.google.android.gms.wallet.WalletConstants
import com.ryftpay.android.core.model.api.RyftEnvironment

internal fun RyftEnvironment.toGooglePayEnvironment(): Int =
    when (this) {
        RyftEnvironment.Prod -> WalletConstants.ENVIRONMENT_PRODUCTION
        else -> WalletConstants.ENVIRONMENT_TEST
    }

internal fun RyftEnvironment.toCheckoutComEnvironment(): Environment =
    when (this) {
        RyftEnvironment.Prod -> Environment.PRODUCTION
        else -> Environment.SANDBOX
    }
