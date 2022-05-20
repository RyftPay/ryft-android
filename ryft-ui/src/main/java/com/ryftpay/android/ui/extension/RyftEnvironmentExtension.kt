package com.ryftpay.android.ui.extension

import com.google.android.gms.wallet.WalletConstants
import com.ryftpay.android.core.model.api.RyftEnvironment

internal fun RyftEnvironment.toGooglePayEnvironment(): Int =
    when (this) {
        RyftEnvironment.Prod -> WalletConstants.ENVIRONMENT_PRODUCTION
        else -> WalletConstants.ENVIRONMENT_TEST
    }
