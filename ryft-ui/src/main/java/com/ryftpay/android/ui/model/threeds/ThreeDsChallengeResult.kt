package com.ryftpay.android.ui.model.threeds

internal sealed class ThreeDsChallengeResult {
    data class Completed(
        val transactionStatus: String,
        val threeDSServerTransactionId: String
    ) : ThreeDsChallengeResult()
    object Cancelled : ThreeDsChallengeResult()
    data class Failed(val message: String) : ThreeDsChallengeResult()
}
