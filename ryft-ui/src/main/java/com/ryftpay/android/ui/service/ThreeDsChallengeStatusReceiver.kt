package com.ryftpay.android.ui.service

import com.ryftpay.android.ui.model.threeds.ThreeDsChallengeResult
import com.ul.emvco3ds.sdk.spec.ChallengeStatusReceiver
import com.ul.emvco3ds.sdk.spec.CompletionEvent
import com.ul.emvco3ds.sdk.spec.ProtocolErrorEvent
import com.ul.emvco3ds.sdk.spec.RuntimeErrorEvent
import java.util.concurrent.atomic.AtomicBoolean

internal class ThreeDsChallengeStatusReceiver(
    private val threeDSServerTransactionId: String,
    private val onResult: (ThreeDsChallengeResult) -> Unit
) : ChallengeStatusReceiver {

    private val resultDelivered = AtomicBoolean(false)

    override fun completed(completionEvent: CompletionEvent?) {
        val transactionStatus = completionEvent?.getTransactionStatus()
        deliver(
            if (transactionStatus == null) {
                ThreeDsChallengeResult.Failed("Challenge completed without a transaction status")
            } else {
                ThreeDsChallengeResult.Completed(
                    transactionStatus = transactionStatus,
                    threeDSServerTransactionId = threeDSServerTransactionId
                )
            }
        )
    }

    override fun cancelled() {
        deliver(ThreeDsChallengeResult.Cancelled)
    }

    override fun timedout() {
        deliver(ThreeDsChallengeResult.Failed("Challenge timed out"))
    }

    override fun protocolError(protocolErrorEvent: ProtocolErrorEvent?) {
        deliver(
            ThreeDsChallengeResult.Failed(
                "Protocol error: ${protocolErrorEvent?.getErrorMessage()?.getErrorDescription()}"
            )
        )
    }

    override fun runtimeError(runtimeErrorEvent: RuntimeErrorEvent?) {
        deliver(
            ThreeDsChallengeResult.Failed(
                "Runtime error: ${runtimeErrorEvent?.getErrorMessage()}"
            )
        )
    }

    private fun deliver(result: ThreeDsChallengeResult) {
        if (resultDelivered.compareAndSet(false, true)) {
            onResult(result)
        }
    }
}
