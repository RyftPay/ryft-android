package com.ryftpay.android.ui.service.request.googlepay

internal data class MerchantInfo(
    val displayName: String
) {
    companion object {
        internal fun from(
            displayName: String
        ): MerchantInfo = MerchantInfo(
            displayName
        )
    }
}
