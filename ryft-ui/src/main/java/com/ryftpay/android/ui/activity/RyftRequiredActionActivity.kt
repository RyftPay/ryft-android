package com.ryftpay.android.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ryftpay.android.core.client.RyftApiClientFactory
import com.ryftpay.android.core.extension.extractPaymentSessionIdFromClientSecret
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.ChallengeAction
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.RequiredAction
import com.ryftpay.android.core.model.payment.RequiredActionType
import com.ryftpay.android.core.service.DefaultRyftPaymentService
import com.ryftpay.android.core.service.RyftPaymentService
import com.ryftpay.android.core.service.listener.RyftRawPaymentResultListener
import com.ryftpay.android.ui.client.RavelinThreeDsServiceFactory
import com.ryftpay.android.ui.dropin.RyftPaymentError
import com.ryftpay.android.ui.model.threeds.ThreeDsChallengeResult
import com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionComponent
import com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionResult
import com.ryftpay.android.ui.extension.getParcelableArgs
import com.ryftpay.android.ui.service.ThreeDsService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.ryftpay.android.ui.util.RequiredActionParceler
import com.ryftpay.android.ui.util.RyftPublicApiKeyParceler
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.lang.IllegalArgumentException

internal class RyftRequiredActionActivity :
    AppCompatActivity(),
    RyftRawPaymentResultListener {

    private val tag: String = RyftRequiredActionActivity::class.java.name

    private lateinit var ryftPaymentService: RyftPaymentService
    private var threeDsServiceDeferred: Deferred<ThreeDsService>? = null
    private lateinit var clientSecret: String
    private var subAccountId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ryft_required_action)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val input = intent.getParcelableArgs(ARGUMENTS_INTENT_EXTRA, Arguments::class.java)
            ?: throw IllegalArgumentException("No arguments provided to activity")
        setupRyftPaymentService(input.publicApiKey)
        setupRavelinThreeDsService(input.publicApiKey)
        clientSecret = input.configuration.clientSecret
        subAccountId = input.configuration.subAccountId
        handleRequiredAction(input.requiredAction)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun onDestroy() {
        super.onDestroy()
        threeDsServiceDeferred?.let { deferred ->
            if (deferred.isCompleted) deferred.getCompleted().cleanup()
            else deferred.cancel()
        }
    }

    override fun onRawPaymentResult(response: PaymentSession) = returnRequiredActionResult(
        RyftRequiredActionResult.Success(response)
    )

    override fun onErrorObtainingPaymentResult(error: RyftError?, throwable: Throwable?) {
        val paymentSessionId = clientSecret.extractPaymentSessionIdFromClientSecret()
        Log.e(tag, "Error obtaining payment result for: $paymentSessionId due to error: $error, throwable: $throwable")
        returnRequiredActionResult(
            RyftRequiredActionResult.Error(
                RyftPaymentError.from(error, throwable, context = this)
            )
        )
    }

    private fun setupRyftPaymentService(publicApiKey: RyftPublicApiKey) {
        ryftPaymentService = DefaultRyftPaymentService(
            RyftApiClientFactory(publicApiKey).createRyftApiClient()
        )
    }

    private fun setupRavelinThreeDsService(publicApiKey: RyftPublicApiKey) {
        val scope = lifecycleScope
        threeDsServiceDeferred = scope.async {
            RavelinThreeDsServiceFactory.create(
                context = applicationContext,
                ryftEnvironment = publicApiKey.getEnvironment(),
                coroutineScope = scope
            )
        }
    }

    private fun handleRequiredAction(
        requiredAction: RequiredAction
    ) = when (requiredAction.type) {
        RequiredActionType.Identify -> handleIdentification(
            requiredAction.identify!!
        )
        else -> returnRequiredActionResult(
            RyftRequiredActionResult.Error(
                RyftPaymentError(
                    displayError = getString(
                        R.string.ryft_unsupported_required_action_developer_error_message,
                        requiredAction.type
                    )
                )
            )
        )
    }

    private fun handleIdentification(
        identifyAction: IdentifyAction
    ) {
        lifecycleScope.launch {
            val transactionParams = threeDsServiceDeferred!!.await().createTransaction(identifyAction)
            ryftPaymentService.continuePayment(
                clientSecret = clientSecret,
                subAccountId = subAccountId,
                threeDsTransactionParams = transactionParams,
                listener = this@RyftRequiredActionActivity
            )
        }
    }

    override fun onPaymentRequiresChallenge(challengeAction: ChallengeAction) {
        lifecycleScope.launch {
            when (val result = threeDsServiceDeferred!!.await().doChallenge(this@RyftRequiredActionActivity, challengeAction)) {
                is ThreeDsChallengeResult.Completed -> ryftPaymentService.continuePaymentAfterChallenge(
                    clientSecret = clientSecret,
                    subAccountId = subAccountId,
                    transactionStatus = result.transactionStatus,
                    threeDSServerTransactionId = result.threeDSServerTransactionId,
                    listener = this@RyftRequiredActionActivity
                )
                is ThreeDsChallengeResult.Cancelled -> returnRequiredActionResult(
                    RyftRequiredActionResult.Error(RyftPaymentError(displayError = "Challenge cancelled"))
                )
                is ThreeDsChallengeResult.Failed -> returnRequiredActionResult(
                    RyftRequiredActionResult.Error(RyftPaymentError(displayError = result.message))
                )
            }
        }
    }

    private fun returnRequiredActionResult(requiredActionResult: RyftRequiredActionResult) {
        setResult(
            RESULT_OK,
            Intent().apply {
                putExtra(
                    REQUIRED_ACTION_RESULT_INTENT_EXTRA,
                    requiredActionResult
                )
            }
        )
        finish()
    }

    @Parcelize
    private class Arguments(
        val configuration: RyftRequiredActionComponent.Configuration,
        val publicApiKey: @WriteWith<RyftPublicApiKeyParceler> RyftPublicApiKey,
        val requiredAction: @WriteWith<RequiredActionParceler> RequiredAction
    ) : Parcelable

    companion object {
        internal const val REQUIRED_ACTION_RESULT_INTENT_EXTRA = "com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionResult"
        private const val ARGUMENTS_INTENT_EXTRA = "com.ryftpay.android.ui.activity.RyftRequiredActionActivity.Arguments"

        internal fun createIntent(
            context: Context,
            configuration: RyftRequiredActionComponent.Configuration,
            publicApiKey: RyftPublicApiKey,
            requiredAction: RequiredAction
        ): Intent = Intent(context, RyftRequiredActionActivity::class.java).apply {
            putExtra(
                ARGUMENTS_INTENT_EXTRA,
                Arguments(
                    configuration,
                    publicApiKey,
                    requiredAction
                )
            )
        }
    }
}
