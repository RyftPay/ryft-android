package com.ryftpay.android.core.model.payment

enum class PaymentSessionError(
    val rawValue: String
) {
    BadTrackData(
        rawValue = "bad_track_data"
    ),
    DeclinedDoNotHonour(
        rawValue = "declined_do_not_honour"
    ),
    ExpiredCard(
        rawValue = "expired_card"
    ),
    GatewayReject(
        rawValue = "gateway_reject"
    ),
    InsufficientFunds(
        rawValue = "insufficient_funds"
    ),
    InvalidCardNumber(
        rawValue = "invalid_card_number"
    ),
    RestrictedCard(
        rawValue = "restricted_card"
    ),
    SecurityViolation(
        rawValue = "security_violation"
    ),
    ThreeDSecureAuthenticationFailure(
        rawValue = "3ds_authentication_failure"
    ),
    Unknown(
        rawValue = "unknown_error"
    );

    companion object {
        private val rawValues = values().associateBy { it.rawValue }

        internal fun from(error: String?): PaymentSessionError? = if (error == null) {
            null
        } else rawValues[error] ?: Unknown
    }
}
