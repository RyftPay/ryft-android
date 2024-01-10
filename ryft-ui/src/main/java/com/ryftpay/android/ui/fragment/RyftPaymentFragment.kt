package com.ryftpay.android.ui.fragment

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ryftpay.android.core.client.RyftApiClientFactory
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.CardDetails
import com.ryftpay.android.core.model.payment.CustomerDetails
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentMethodOptions
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionError
import com.ryftpay.android.core.service.DefaultRyftPaymentService
import com.ryftpay.android.core.service.RyftPaymentService
import com.ryftpay.android.core.service.listener.RyftLoadPaymentListener
import com.ryftpay.android.core.service.listener.RyftPaymentResultListener
import com.ryftpay.android.ui.client.Checkout3dsServiceFactory
import com.ryftpay.android.ui.client.PaymentsClientFactory
import com.ryftpay.android.ui.delegate.DefaultRyftPaymentDelegate
import com.ryftpay.android.ui.delegate.RyftPaymentDelegate
import com.ryftpay.android.ui.dropin.RyftDropInConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInDisplayConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInFieldCollectionConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInGooglePayConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInUsage
import com.ryftpay.android.ui.dropin.RyftPaymentError
import com.ryftpay.android.ui.dropin.RyftPaymentResult
import com.ryftpay.android.ui.extension.getParcelableArgs
import com.ryftpay.android.ui.extension.showAlertWithRetry
import com.ryftpay.android.ui.listener.RyftPaymentFormListener
import com.ryftpay.android.ui.model.RyftCard
import com.ryftpay.android.ui.model.RyftCardOptions
import com.ryftpay.android.ui.model.googlepay.GooglePayResult
import com.ryftpay.android.ui.model.googlepay.LoadPaymentDataRequest
import com.ryftpay.android.ui.model.googlepay.MerchantInfo
import com.ryftpay.android.ui.model.googlepay.TokenizationSpecification
import com.ryftpay.android.ui.model.googlepay.TransactionInfo
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResult
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResultListener
import com.ryftpay.android.ui.service.DefaultCheckoutThreeDsService
import com.ryftpay.android.ui.service.DefaultGooglePayService
import com.ryftpay.android.ui.service.GooglePayService
import com.ryftpay.android.ui.service.ThreeDsService
import com.ryftpay.android.ui.util.RyftPublicApiKeyParceler
import com.ryftpay.android.ui.viewmodel.GooglePayResultViewModel
import com.ryftpay.android.ui.viewmodel.RyftPaymentResultViewModel
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.lang.IllegalArgumentException

internal class RyftPaymentFragment :
    BottomSheetDialogFragment(),
    RyftPaymentFormListener,
    RyftPaymentResultListener,
    RyftLoadPaymentListener,
    ThreeDsIdentificationResultListener {

    private lateinit var delegate: RyftPaymentDelegate
    private lateinit var ryftPaymentService: RyftPaymentService
    private lateinit var paymentResultViewModel: RyftPaymentResultViewModel
    private lateinit var threeDsService: ThreeDsService
    private lateinit var publicApiKey: RyftPublicApiKey
    private lateinit var clientSecret: String
    private lateinit var displayConfiguration: RyftDropInDisplayConfiguration
    private lateinit var fieldCollectionConfiguration: RyftDropInFieldCollectionConfiguration
    private var testMode: Boolean = false
    private var subAccountId: String? = null
    private var googlePayConfiguration: RyftDropInGooglePayConfiguration? = null
    private var googlePayService: GooglePayService? = null
    private var googlePayResultViewModel: GooglePayResultViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val input: Arguments = arguments?.getParcelableArgs(
            ARGUMENTS_BUNDLE_KEY,
            Arguments::class.java
        ) ?: throw IllegalArgumentException("No arguments provided to fragment")
        publicApiKey = input.publicApiKey
        clientSecret = input.configuration.clientSecret
        subAccountId = input.configuration.subAccountId
        googlePayConfiguration = input.configuration.googlePayConfiguration
        testMode = input.testMode
        displayConfiguration = input.configuration.display
        fieldCollectionConfiguration = input.configuration.fieldCollection
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.fragment_ryft_payment, container, false)
    }

    override fun onViewCreated(
        root: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(root, savedInstanceState)
        setupRyftPaymentService()
        setupPaymentResultViewModel()
        setupThreeDSecureResultObserver()
        val googlePayEnabled = googlePayConfiguration != null &&
            displayConfiguration.usage == RyftDropInUsage.Payment
        if (!googlePayEnabled) {
            initialiseDelegate(root, googlePayAvailable = false)
            return
        }
        setupGooglePayService()
        setupGooglePayResultViewModel()
        checkGooglePayIsAvailableBeforeInitialisingDelegate(root)
    }

    override fun getTheme(): Int = R.style.RyftBottomSheetDialog

    override fun onCancel() {
        paymentResultViewModel.updateResult(RyftPaymentResult.Cancelled)
        dismiss()
    }

    override fun onPayByCard(card: RyftCard, cardOptions: RyftCardOptions) {
        if (testMode) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 3000)
            return
        }
        dialog?.setCancelable(false)
        ryftPaymentService.attemptPayment(
            clientSecret = clientSecret,
            paymentMethod = PaymentMethod.card(
                CardDetails(
                    card.number.sanitisedNumber,
                    card.expiryDate.twoDigitMonth,
                    card.expiryDate.fourDigitYear,
                    card.cvc.sanitisedCvc,
                    card.name?.sanitisedName
                ),
                PaymentMethodOptions.card(
                    store = displayConfiguration.usage == RyftDropInUsage.SetupCard ||
                        cardOptions.saveForFuture
                )
            ),
            customerDetails = null,
            subAccountId = subAccountId,
            listener = this
        )
    }

    override fun onPayByGooglePay() {
        if (testMode) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 3000)
            return
        }
        dialog?.setCancelable(false)
        ryftPaymentService.loadPaymentSession(
            clientSecret = clientSecret,
            subAccountId = subAccountId,
            listener = this
        )
    }

    override fun onPaymentApproved(response: PaymentSession) {
        paymentResultViewModel.updateResult(RyftPaymentResult.Approved(response))
        safeDismiss()
    }

    override fun onPaymentRequiresRedirect(
        returnUrl: String,
        redirectUrl: String
    ) = findNavController().navigate(
        R.id.action_ryft_payment_to_ryft_three_d_secure,
        RyftThreeDSecureFragment.Arguments.bundleFrom(
            Uri.parse(redirectUrl),
            Uri.parse(returnUrl)
        )
    )

    override fun onPaymentRequiresIdentification(
        returnUrl: String,
        identifyAction: IdentifyAction
    ) {
        threeDsService = DefaultCheckoutThreeDsService(
            Checkout3dsServiceFactory.create(
                requireContext(),
                publicApiKey.getEnvironment(),
                returnUrl
            )
        )
        threeDsService.handleIdentification(
            identifyAction,
            listener = this
        )
    }

    override fun onPaymentHasError(lastError: PaymentSessionError) {
        paymentResultViewModel.updateResult(
            RyftPaymentResult.Failed(RyftPaymentError.from(lastError, requireContext()))
        )
        safeDismiss()
    }

    override fun onErrorObtainingPaymentResult(
        error: RyftError?,
        throwable: Throwable?
    ) {
        paymentResultViewModel.updateResult(
            RyftPaymentResult.Failed(RyftPaymentError.from(throwable, requireContext()))
        )
        safeDismiss()
    }

    override fun onErrorLoadingPayment(
        error: RyftError?,
        throwable: Throwable?
    ) {
        val stringResourceId = when (error?.httpStatusCode) {
            "400" -> R.string.ryft_invalid_client_secret_developer_error_message
            "403" -> R.string.ryft_invalid_api_key_developer_error_message
            "404" -> R.string.ryft_payment_not_found_developer_error_message
            else -> R.string.ryft_google_pay_error_message
        }
        showAlertWithRetry(
            message = getString(stringResourceId),
            retryCallback = { onPayByGooglePay() },
            cancelCallback = { delegate.onGooglePayFailedOrCancelled() }
        )
    }

    override fun onPaymentLoaded(response: PaymentSession) {
        val validGooglePayConfiguration = googlePayConfiguration ?: return
        googlePayService!!.loadPaymentData(
            activity = requireActivity(),
            loadPaymentDataRequest = LoadPaymentDataRequest(
                MerchantInfo.from(validGooglePayConfiguration.merchantName),
                validGooglePayConfiguration.billingAddressRequired,
                TokenizationSpecification.ryft(publicApiKey),
                TransactionInfo.from(
                    response,
                    validGooglePayConfiguration.merchantCountryCode
                ),
                emailRequired = response.customerEmail == null
            )
        )
    }

    override fun onThreeDsIdentificationResult(
        result: ThreeDsIdentificationResult,
        paymentMethodId: String
    ) {
        ryftPaymentService.attemptPayment(
            clientSecret = clientSecret,
            paymentMethod = PaymentMethod.id(
                id = paymentMethodId
            ),
            customerDetails = null,
            subAccountId = subAccountId,
            listener = this
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        paymentResultViewModel.updateResult(RyftPaymentResult.Cancelled)
    }

    private fun setupRyftPaymentService() {
        ryftPaymentService = DefaultRyftPaymentService(
            RyftApiClientFactory(publicApiKey).createRyftApiClient()
        )
    }

    private fun setupPaymentResultViewModel() {
        paymentResultViewModel = ViewModelProvider(requireActivity())[RyftPaymentResultViewModel::class.java]
    }

    private fun setupThreeDSecureResultObserver() {
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<RyftThreeDSecureFragment.Result>(RyftThreeDSecureFragment.RESULT_BUNDLE_KEY)
            ?.observe(viewLifecycleOwner) { result ->
                ryftPaymentService.getLatestPaymentResult(
                    result.paymentSessionId,
                    clientSecret,
                    subAccountId,
                    listener = this
                )
            }
    }

    private fun setupGooglePayService() {
        googlePayService = DefaultGooglePayService(
            PaymentsClientFactory.createPaymentsClient(
                publicApiKey.getEnvironment(),
                activity = requireActivity()
            )
        )
    }

    private fun setupGooglePayResultViewModel() {
        googlePayResultViewModel = ViewModelProvider(requireActivity())[GooglePayResultViewModel::class.java]
        googlePayResultViewModel!!.getResult().observe(this) { result ->
            handleGooglePayResult(result)
        }
    }

    private fun handleGooglePayResult(result: GooglePayResult) = when (result) {
        is GooglePayResult.Ok -> {
            delegate.onGooglePayPaymentProcessing()
            val paymentData = result.paymentData
            ryftPaymentService.attemptPayment(
                clientSecret = clientSecret,
                paymentMethod = PaymentMethod.googlePay(
                    paymentData.token,
                    paymentData.billingAddress
                ),
                customerDetails = CustomerDetails.from(paymentData.email),
                subAccountId = subAccountId,
                listener = this
            )
        }
        else -> {
            delegate.onGooglePayFailedOrCancelled()
        }
    }

    private fun checkGooglePayIsAvailableBeforeInitialisingDelegate(root: View) {
        val validGooglePayConfiguration = googlePayConfiguration ?: return
        googlePayService!!.isReadyToPay(
            validGooglePayConfiguration.existingPaymentMethodRequired,
            validGooglePayConfiguration.billingAddressRequired
        ).addOnCompleteListener { completedTask ->
            run {
                val googlePayAvailable = try {
                    completedTask.getResult(ApiException::class.java)
                } catch (exception: ApiException) {
                    false
                }
                initialiseDelegate(root, googlePayAvailable)
            }
        }
    }

    private fun initialiseDelegate(
        root: View,
        googlePayAvailable: Boolean
    ) {
        delegate = DefaultRyftPaymentDelegate(listener = this)
        delegate.onViewCreated(
            root,
            displayConfiguration.usage,
            displayConfiguration.payButtonTitle,
            googlePayAvailable,
            fieldCollectionConfiguration.nameOnCard
        )
    }

    private fun safeDismiss() {
        if (isResumed) dismiss() else dismissAllowingStateLoss()
    }

    @Parcelize
    internal class Arguments(
        val configuration: RyftDropInConfiguration,
        val publicApiKey: @WriteWith<RyftPublicApiKeyParceler> RyftPublicApiKey,
        val testMode: Boolean
    ) : Parcelable {
        companion object {
            fun bundleFrom(
                configuration: RyftDropInConfiguration,
                publicApiKey: RyftPublicApiKey
            ): Bundle = Bundle().apply {
                putParcelable(
                    ARGUMENTS_BUNDLE_KEY,
                    Arguments(configuration, publicApiKey, testMode = false)
                )
            }
        }
    }

    companion object {
        @VisibleForTesting
        internal const val ARGUMENTS_BUNDLE_KEY = "Arguments"
    }
}
