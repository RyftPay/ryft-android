package com.ryftpay.android.core.model.payment

enum class RequiredActionType {
    Redirect,
    Identify,
    Unknown;

    companion object {
        internal fun from(response: String): RequiredActionType {
            return when (response) {
                "Redirect" -> Redirect
                "Identify" -> Identify
                else -> Unknown
            }
        }
    }
}
