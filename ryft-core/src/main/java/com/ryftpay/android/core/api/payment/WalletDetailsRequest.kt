package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty

data class WalletDetailsRequest(
    @JsonProperty("type") val type: String,
    @JsonProperty("googlePayToken") val googlePayToken: String
) {
    companion object {
        private const val GOOGLE_PAY_WALLET_TYPE = "GooglePay"

        internal fun from(googlePayToken: String): WalletDetailsRequest =
            WalletDetailsRequest(
                GOOGLE_PAY_WALLET_TYPE,
                googlePayToken
            )
    }
}
