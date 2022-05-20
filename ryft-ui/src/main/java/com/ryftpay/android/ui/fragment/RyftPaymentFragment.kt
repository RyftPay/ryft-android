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
import androidx.annotation.VisibleForTesting.PRIVATE
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ryftpay.android.core.client.RyftApiClientFactory
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.CardDetails
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionError
import com.ryftpay.android.core.service.DefaultRyftPaymentService
import com.ryftpay.android.core.service.RyftPaymentService
import com.ryftpay.android.core.service.listener.RyftLoadPaymentListener
import com.ryftpay.android.core.service.listener.RyftPaymentResultListener
import com.ryftpay.android.ui.client.PaymentsClientFactory
import com.ryftpay.android.ui.delegate.DefaultRyftPaymentDelegate
import com.ryftpay.android.ui.delegate.RyftPaymentDelegate
import com.ryftpay.android.ui.dropin.RyftDropInConfiguration
import com.ryftpay.android.ui.dropin.RyftPaymentError
import com.ryftpay.android.ui.dropin.RyftPaymentResult
import com.ryftpay.android.ui.extension.showAlertWithRetry
import com.ryftpay.android.ui.listener.RyftPaymentFormListener
import com.ryftpay.android.ui.model.RyftCard
import com.ryftpay.android.ui.model.googlepay.GooglePayResult
import com.ryftpay.android.ui.model.googlepay.LoadPaymentDataRequest
import com.ryftpay.android.ui.model.googlepay.MerchantInfo
import com.ryftpay.android.ui.model.googlepay.TokenizationSpecification
import com.ryftpay.android.ui.model.googlepay.TransactionInfo
import com.ryftpay.android.ui.service.DefaultGooglePayService
import com.ryftpay.android.ui.service.GooglePayService
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
    RyftLoadPaymentListener {

    private lateinit var delegate: RyftPaymentDelegate
    private lateinit var input: Arguments
    private lateinit var ryftPaymentService: RyftPaymentService
    private lateinit var paymentResultViewModel: RyftPaymentResultViewModel
    private var googlePayService: GooglePayService? = null
    private var googlePayResultViewModel: GooglePayResultViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        input = arguments?.getParcelable(ARGUMENTS_BUNDLE_KEY)
            ?: throw IllegalArgumentException("No arguments provided to fragment")
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
        if (!input.configuration.googlePayEnabled) {
            initialiseDelegate(root, showGooglePay = false)
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

    override fun onPayByCard(card: RyftCard) {
        if (input.testMode) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 3000)
            return
        }
        dialog?.setCancelable(false)
        ryftPaymentService.attemptPayment(
            clientSecret = input.configuration.clientSecret,
            paymentMethod = PaymentMethod.card(
                CardDetails(
                    card.number.sanitisedNumber,
                    card.expiryDate.twoDigitMonth,
                    card.expiryDate.fourDigitYear,
                    card.cvc.sanitisedCvc
                )
            ),
            subAccountId = input.configuration.subAccountId,
            listener = this
        )
    }

    override fun onPayByGooglePay() {
        if (input.testMode) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 3000)
            return
        }
        dialog?.setCancelable(false)
        ryftPaymentService.loadPaymentSession(
            clientSecret = input.configuration.clientSecret,
            subAccountId = input.configuration.subAccountId,
            listener = this
        )
    }

    override fun onPaymentApproved(response: PaymentSession) {
        paymentResultViewModel.updateResult(RyftPaymentResult.Approved)
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
        showAlertWithRetry(
            message = getString(R.string.ryft_google_pay_error_message),
            retryCallback = { onPayByGooglePay() },
            cancelCallback = { delegate.onGooglePayFailedOrCancelled() }
        )
    }

    override fun onPaymentLoaded(response: PaymentSession) {
        val googlePayConfiguration = input.configuration.googlePayConfiguration ?: return
        googlePayService!!.loadPaymentData(
            activity = requireActivity(),
            loadPaymentDataRequest = LoadPaymentDataRequest(
                MerchantInfo.from(googlePayConfiguration.merchantName),
                googlePayConfiguration.billingAddressRequired,
                TokenizationSpecification.ryft(input.publicApiKey),
                TransactionInfo.from(
                    response,
                    googlePayConfiguration.merchantCountryCode
                )
            )
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        paymentResultViewModel.updateResult(RyftPaymentResult.Cancelled)
    }

    private fun setupRyftPaymentService() {
        ryftPaymentService = DefaultRyftPaymentService(
            RyftApiClientFactory(input.publicApiKey).createRyftApiClient()
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
                    input.configuration.clientSecret,
                    input.configuration.subAccountId,
                    listener = this
                )
            }
    }

    private fun setupGooglePayService() {
        googlePayService = DefaultGooglePayService(
            PaymentsClientFactory.createPaymentsClient(
                input.publicApiKey.getEnvironment(),
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

    private fun handleGooglePayResult(result: GooglePayResult) {
        when (result) {
            is GooglePayResult.Ok -> {
                delegate.onGooglePayPaymentProcessing()
                ryftPaymentService.attemptPayment(
                    clientSecret = input.configuration.clientSecret,
                    paymentMethod = PaymentMethod.googlePay(
                        result.paymentData.token,
                        result.paymentData.billingAddress
                    ),
                    subAccountId = input.configuration.subAccountId,
                    listener = this
                )
            }
            else -> {
                delegate.onGooglePayFailedOrCancelled()
            }
        }
    }

    private fun checkGooglePayIsAvailableBeforeInitialisingDelegate(root: View) {
        val googlePayConfiguration = input.configuration.googlePayConfiguration ?: return
        googlePayService!!.isReadyToPay(
            googlePayConfiguration.billingAddressRequired
        ).addOnCompleteListener { completedTask ->
            run {
                val showGooglePay = try {
                    completedTask.getResult(ApiException::class.java)
                } catch (exception: ApiException) {
                    false
                }
                initialiseDelegate(root, showGooglePay)
            }
        }
    }

    private fun initialiseDelegate(root: View, showGooglePay: Boolean) {
        delegate = DefaultRyftPaymentDelegate(listener = this)
        delegate.onViewCreated(root, showGooglePay)
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
        @VisibleForTesting(otherwise = PRIVATE)
        internal const val ARGUMENTS_BUNDLE_KEY = "Arguments"
    }
}
