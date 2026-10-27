package com.ryftpay.android.ui.service

import com.ryftpay.android.ui.model.threeds.ThreeDsChallengeResult
import com.ul.emvco3ds.sdk.spec.CompletionEvent
import com.ul.emvco3ds.sdk.spec.ErrorMessage
import com.ul.emvco3ds.sdk.spec.ProtocolErrorEvent
import com.ul.emvco3ds.sdk.spec.RuntimeErrorEvent
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class ThreeDsChallengeStatusReceiverTest {

    private val results = mutableListOf<ThreeDsChallengeResult>()
    private val receiver = ThreeDsChallengeStatusReceiver(THREE_DS_SERVER_TRANSACTION_ID) { results.add(it) }

    @Test
    fun `completed should deliver completed result with transaction status`() {
        receiver.completed(completionEvent(transactionStatus = "Y"))

        results shouldBeEqualTo listOf(
            ThreeDsChallengeResult.Completed(
                transactionStatus = "Y",
                threeDSServerTransactionId = THREE_DS_SERVER_TRANSACTION_ID
            )
        )
    }

    @Test
    fun `completed should deliver failed result when completion event is null`() {
        receiver.completed(null)

        results shouldBeEqualTo listOf(
            ThreeDsChallengeResult.Failed("Challenge completed without a transaction status")
        )
    }

    @Test
    fun `completed should deliver failed result when transaction status is null`() {
        receiver.completed(completionEvent(transactionStatus = null))

        results shouldBeEqualTo listOf(
            ThreeDsChallengeResult.Failed("Challenge completed without a transaction status")
        )
    }

    @Test
    fun `cancelled should deliver cancelled result`() {
        receiver.cancelled()

        results shouldBeEqualTo listOf(ThreeDsChallengeResult.Cancelled)
    }

    @Test
    fun `timedout should deliver failed result`() {
        receiver.timedout()

        results shouldBeEqualTo listOf(ThreeDsChallengeResult.Failed("Challenge timed out"))
    }

    @Test
    fun `protocolError should deliver failed result with error description`() {
        val errorMessage = mockk<ErrorMessage> {
            every { getErrorDescription() } returns "Invalid message"
        }
        val event = mockk<ProtocolErrorEvent> {
            every { getErrorMessage() } returns errorMessage
        }

        receiver.protocolError(event)

        results shouldBeEqualTo listOf(ThreeDsChallengeResult.Failed("Protocol error: Invalid message"))
    }

    @Test
    fun `runtimeError should deliver failed result with error message`() {
        val event = mockk<RuntimeErrorEvent> {
            every { getErrorMessage() } returns "Something went wrong"
        }

        receiver.runtimeError(event)

        results shouldBeEqualTo listOf(ThreeDsChallengeResult.Failed("Runtime error: Something went wrong"))
    }

    @Test
    fun `should only deliver the first result when multiple terminal callbacks fire`() {
        receiver.cancelled()
        receiver.runtimeError(null)
        receiver.timedout()
        receiver.completed(completionEvent(transactionStatus = "Y"))
        receiver.protocolError(null)

        results shouldBeEqualTo listOf(ThreeDsChallengeResult.Cancelled)
    }

    @Test
    fun `should only deliver the first result when completed fires before a runtime error`() {
        receiver.completed(completionEvent(transactionStatus = "Y"))
        receiver.runtimeError(null)

        results shouldBeEqualTo listOf(
            ThreeDsChallengeResult.Completed(
                transactionStatus = "Y",
                threeDSServerTransactionId = THREE_DS_SERVER_TRANSACTION_ID
            )
        )
    }

    private fun completionEvent(transactionStatus: String?) = mockk<CompletionEvent> {
        every { getTransactionStatus() } returns transactionStatus
    }

    companion object {
        private const val THREE_DS_SERVER_TRANSACTION_ID = "3ds_server_transaction_id"
    }
}
