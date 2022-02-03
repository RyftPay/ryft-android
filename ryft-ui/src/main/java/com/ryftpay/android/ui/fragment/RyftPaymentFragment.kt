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
import com.ryftpay.android.core.service.listener.RyftPaymentListener
import com.ryftpay.android.ui.delegate.DefaultRyftPaymentDelegate
import com.ryftpay.android.ui.delegate.RyftPaymentDelegate
import com.ryftpay.android.ui.dropin.RyftDropInConfiguration
import com.ryftpay.android.ui.dropin.RyftPaymentError
import com.ryftpay.android.ui.dropin.RyftPaymentResult
import com.ryftpay.android.ui.listener.RyftPaymentFormListener
import com.ryftpay.android.ui.model.RyftCard
import com.ryftpay.android.ui.util.RyftPublicApiKeyParceler
import com.ryftpay.android.ui.viewmodel.RyftPaymentResultViewModel
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.lang.IllegalArgumentException

internal class RyftPaymentFragment :
    BottomSheetDialogFragment(),
    RyftPaymentFormListener,
    RyftPaymentListener {

    private lateinit var delegate: RyftPaymentDelegate
    private lateinit var input: Arguments
    private lateinit var service: RyftPaymentService
    private lateinit var paymentResultViewModel: RyftPaymentResultViewModel

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
        service = DefaultRyftPaymentService(
            RyftApiClientFactory(input.publicApiKey).createRyftApiClient(),
            paymentListener = this
        )
        setupPaymentResultViewModel()
        setupThreeDSecureResultObserver()
        delegate = DefaultRyftPaymentDelegate(listener = this)
        delegate.onViewCreated(root)
    }

    override fun getTheme(): Int = R.style.RyftBottomSheetDialog

    override fun onCancel() {
        paymentResultViewModel.updateResult(RyftPaymentResult.Cancelled)
        dismiss()
    }

    override fun onPay(card: RyftCard) {
        if (input.testMode) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 3000)
            return
        }
        dialog?.setCancelable(false)
        service.attemptPayment(
            clientSecret = input.configuration.clientSecret,
            paymentMethod = PaymentMethod.card(
                CardDetails(
                    card.number.sanitisedNumber,
                    card.expiryDate.twoDigitMonth,
                    card.expiryDate.fourDigitYear,
                    card.cvc.sanitisedCvc
                )
            ),
            subAccountId = input.configuration.subAccountId
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

    override fun onError(
        error: RyftError?,
        throwable: Throwable?
    ) {
        paymentResultViewModel.updateResult(
            RyftPaymentResult.Failed(RyftPaymentError.from(throwable, requireContext()))
        )
        safeDismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        paymentResultViewModel.updateResult(RyftPaymentResult.Cancelled)
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
                service.loadPaymentSession(
                    result.paymentSessionId,
                    input.configuration.clientSecret,
                    input.configuration.subAccountId
                )
            }
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
