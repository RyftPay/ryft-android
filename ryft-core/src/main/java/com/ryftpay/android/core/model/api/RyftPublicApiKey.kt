package com.ryftpay.android.core.model.api

data class RyftPublicApiKey(
    val value: String
) {
    init {
        require(value.startsWith(KEY_PREFIX)) { "The API key you have provided is invalid, please check you are using your public API key" }
    }

    fun getEnvironment(): RyftEnvironment =
        if (value.startsWith(SANDBOX_KEY_PREFIX)) {
            RyftEnvironment.Sandbox
        } else RyftEnvironment.Prod

    companion object {
        private const val KEY_PREFIX = "pk_"
        private const val SANDBOX_KEY_PREFIX = "${KEY_PREFIX}sandbox_"
    }
}
