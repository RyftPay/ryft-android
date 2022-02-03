package com.ryftpay.android.core.model.payment

enum class PaymentSessionStatus {
    PendingPayment,
    PendingAction,
    Approved,
    Captured,
    Unknown;

    companion object {
        internal fun from(response: String): PaymentSessionStatus {
            return when (response) {
                "PendingPayment" -> PendingPayment
                "PendingAction" -> PendingAction
                "Approved" -> Approved
                "Captured" -> Captured
                else -> Unknown
            }
        }
    }
}
