package com.ryftpay.android.core.model.payment

enum class RequiredActionType {
    Redirect,
    Identify,
    Challenge,
    Unknown;

    companion object {
        internal fun from(response: String): RequiredActionType {
            return when (response) {
                "Redirect" -> Redirect
                "Identify" -> Identify
                "Challenge" -> Challenge
                else -> Unknown
            }
        }
    }
}
