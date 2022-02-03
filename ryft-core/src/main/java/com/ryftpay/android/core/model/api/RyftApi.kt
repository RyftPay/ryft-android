package com.ryftpay.android.core.model.api

internal object RyftApi {
    private const val SANDBOX_API_URL = "https://sandbox-api.ryftpay.com/v1/"
    private const val PROD_API_URL = "https://api.ryftpay.com/v1/"

    internal fun generateBaseApiUrl(publicApiKey: RyftPublicApiKey): String =
        if (publicApiKey.getEnvironment() == RyftEnvironment.Sandbox) {
            SANDBOX_API_URL
        } else PROD_API_URL
}
