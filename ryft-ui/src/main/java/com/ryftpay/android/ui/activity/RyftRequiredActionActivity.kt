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
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.RequiredAction
import com.ryftpay.android.core.model.payment.RequiredActionType
import com.ryftpay.android.core.service.DefaultRyftPaymentService
import com.ryftpay.android.core.service.RyftPaymentService
import com.ryftpay.android.core.service.listener.RyftRawPaymentResultListener
import com.ryftpay.android.ui.client.Checkout3dsServiceFactory
import com.ryftpay.android.ui.dropin.RyftPaymentError
import com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionComponent
import com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionResult
import com.ryftpay.android.ui.extension.getParcelableArgs
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResult
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResultListener
import com.ryftpay.android.ui.service.DefaultCheckoutThreeDsService
import com.ryftpay.android.ui.service.ThreeDsService
import com.ryftpay.android.ui.util.RequiredActionParceler
import com.ryftpay.android.ui.util.RyftPublicApiKeyParceler
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.lang.IllegalArgumentException

internal class RyftRequiredActionActivity :
    AppCompatActivity(),
    ThreeDsIdentificationResultListener,
    RyftRawPaymentResultListener {

    private val tag: String = RyftRequiredActionActivity::class.java.name

    private lateinit var ryftPaymentService: RyftPaymentService
    private lateinit var threeDsService: ThreeDsService
    private lateinit var clientSecret: String
    private var subAccountId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ryft_required_action)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val input = intent.getParcelableArgs(ARGUMENTS_INTENT_EXTRA, Arguments::class.java)
            ?: throw IllegalArgumentException("No arguments provided to activity")
        setupRyftPaymentService(input.publicApiKey)
        clientSecret = input.configuration.clientSecret
        subAccountId = input.configuration.subAccountId
        handleRequiredAction(
            input.requiredAction,
            input.publicApiKey,
            input.configuration.returnUrl
        )
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    override fun onThreeDsIdentificationResult(
        result: ThreeDsIdentificationResult,
        paymentMethodId: String
    ) = ryftPaymentService.attemptPayment(
        clientSecret = clientSecret,
        paymentMethod = PaymentMethod.id(
            id = paymentMethodId
        ),
        customerDetails = null,
        subAccountId = subAccountId,
        listener = this
    )

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

    private fun handleRequiredAction(
        requiredAction: RequiredAction,
        publicApiKey: RyftPublicApiKey,
        returnUrl: String?
    ) = when (requiredAction.type) {
        RequiredActionType.Identify -> handleIdentification(
            requiredAction.identify!!,
            publicApiKey,
            returnUrl
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
        identifyAction: IdentifyAction,
        publicApiKey: RyftPublicApiKey,
        returnUrl: String?
    ) {
        threeDsService = DefaultCheckoutThreeDsService(
            Checkout3dsServiceFactory.create(
                context = this,
                publicApiKey.getEnvironment(),
                returnUrl
            )
        )
        threeDsService.handleIdentification(
            identifyAction,
            listener = this
        )
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
