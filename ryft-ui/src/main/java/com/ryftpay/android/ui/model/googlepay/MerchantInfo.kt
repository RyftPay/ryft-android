package com.ryftpay.android.ui.model.googlepay

import org.json.JSONObject

internal data class MerchantInfo(
    val displayName: String
) {
    internal fun toApiV2RequestJson(): JSONObject = JSONObject()
        .put(MERCHANT_NAME_KEY, displayName)

    companion object {
        internal const val KEY = "merchantInfo"
        private const val MERCHANT_NAME_KEY = "merchantName"

        internal fun from(
            displayName: String
        ): MerchantInfo = MerchantInfo(
            displayName
        )
    }
}
