package com.ryftpay.android.ui.service

import android.app.Activity
import android.content.Context
import com.ravelin.core.transaction.challenge.ChallengeParameters
import com.ul.emvco3ds.sdk.spec.ChallengeStatusReceiver
import com.ul.emvco3ds.sdk.spec.CompletionEvent
import com.ul.emvco3ds.sdk.spec.ProtocolErrorEvent
import com.ul.emvco3ds.sdk.spec.RuntimeErrorEvent
import com.ul.emvco3ds.sdk.spec.ThreeDS2Service
import com.ul.emvco3ds.sdk.spec.Transaction
import com.ryftpay.android.core.model.api.RyftEnvironment
import com.ryftpay.android.core.model.payment.ChallengeAction
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.ThreeDsTransactionParams
import com.ryftpay.android.ui.extension.toDirectoryServerId
import com.ryftpay.android.ui.model.threeds.ThreeDsChallengeResult
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal class DefaultRavelinThreeDsService(
    private val context: Context,
    private val threeDS2Service: ThreeDS2Service,
    private val environment: RyftEnvironment
) : ThreeDsService {

    private var transaction: Transaction? = null

    override suspend fun createTransaction(identifyAction: IdentifyAction): ThreeDsTransactionParams {
        val directoryServerId = identifyAction.scheme.toDirectoryServerId(environment)
        val protocolVersion = resolveProtocolVersion(identifyAction.protocolVersion, environment)
        Log.d(TAG, "createTransaction: scheme=${identifyAction.scheme}, directoryServerId=$directoryServerId, protocolVersion=$protocolVersion (original=${identifyAction.protocolVersion})")
        val tx = threeDS2Service.createTransaction(
            directoryServerId,
            protocolVersion
        )
        transaction = tx
        val params = requireNotNull(tx.getAuthenticationRequestParameters()) {
            "Ravelin SDK returned null AuthenticationRequestParameters"
        }
        return ThreeDsTransactionParams(
            sdkTransactionId = params.getSDKTransactionID()!!,
            sdkApplicationId = params.getSDKAppID()!!,
            sdkEncryptedData = params.getDeviceData()!!,
            sdkEphemeralPublicKey = params.getSDKEphemeralPublicKey()!!,
            sdkReferenceNumber = params.getSDKReferenceNumber()!!
        )
    }

    override suspend fun doChallenge(
        activity: Activity,
        challengeAction: ChallengeAction
    ): ThreeDsChallengeResult = suspendCancellableCoroutine { cont ->
        val challengeParameters = ChallengeParameters().apply {
            set3DSServerTransactionID(challengeAction.threeDSServerTransactionId)
            setAcsTransactionID(challengeAction.acsTransactionId)
            setAcsRefNumber(challengeAction.acsRefNumber)
            setAcsSignedContent(challengeAction.acsSignedContent)
        }
        val receiver = object : ChallengeStatusReceiver {
            override fun completed(completionEvent: CompletionEvent?) {
                val event = requireNotNull(completionEvent) {
                    "Ravelin SDK returned null CompletionEvent"
                }

                cont.resume(
                    ThreeDsChallengeResult.Completed(
                        transactionStatus = requireNotNull(event.getTransactionStatus()) {
                            "Ravelin SDK returned null transactionStatus"
                        },
                        threeDSServerTransactionId = challengeAction.threeDSServerTransactionId
                    )
                )
            }
            override fun cancelled() {
                cont.resume(ThreeDsChallengeResult.Cancelled)
            }
            override fun timedout() {
                cont.resume(ThreeDsChallengeResult.Failed("Challenge timed out"))
            }

            override fun protocolError(protocolErrorEvent: ProtocolErrorEvent?) {
                cont.resume(
                    ThreeDsChallengeResult.Failed(
                        "Protocol error: ${protocolErrorEvent?.getErrorMessage()?.getErrorDescription()}"
                    )
                )
            }

            override fun runtimeError(runtimeErrorEvent: RuntimeErrorEvent?) {
                cont.resume(
                    ThreeDsChallengeResult.Failed(
                        "Runtime error: ${runtimeErrorEvent?.getErrorMessage()}"
                    )
                )
            }
        }
        transaction!!.doChallenge(activity, challengeParameters, receiver, CHALLENGE_TIMEOUT_MINUTES)
    }

    override fun cleanup() {
        transaction?.close()
        threeDS2Service.cleanup(context)
    }

    companion object {
        private const val TAG = "DefaultRavelinThreeDsService"
        private const val CHALLENGE_TIMEOUT_MINUTES = 5
        private const val MIN_SUPPORTED_PROTOCOL_VERSION = "2.2.0"

        internal fun resolveProtocolVersion(protocolVersion: String, environment: RyftEnvironment): String =
            if (environment != RyftEnvironment.Prod && protocolVersion < MIN_SUPPORTED_PROTOCOL_VERSION) {
                MIN_SUPPORTED_PROTOCOL_VERSION
            } else {
                protocolVersion
            }
    }
}
