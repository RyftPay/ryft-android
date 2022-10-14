package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ryftpay.android.ui.dropin.RyftDropInUsage
import com.ryftpay.android.ui.listener.RyftPaymentFormFooterListener
import com.ryftpay.ui.R

internal class RyftPaymentFormFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var payButton: RyftButton
    private lateinit var cancelButton: RyftButton
    private lateinit var usage: RyftDropInUsage
    private var state: State = State.AwaitingCardDetails
    private var payButtonTitleOverride: String? = null

    internal fun initialise(
        usage: RyftDropInUsage,
        payButtonTitleOverride: String?,
        listener: RyftPaymentFormFooterListener
    ) {
        this.payButton = findViewById(R.id.button_ryft_pay)
        this.cancelButton = findViewById(R.id.button_ryft_cancel)
        this.usage = usage
        this.payButtonTitleOverride = payButtonTitleOverride

        payButton.initialise(
            determinePayButtonTitle(),
            enabled = false,
            clickListener = {
                toggleButtons(enabled = false)
                listener.onPayClicked()
            },
            opacityWhenDisabled = 0.7F
        )
        cancelButton.initialise(
            context.getString(R.string.ryft_cancel),
            enabled = true,
            clickListener = {
                toggleButtons(enabled = false)
                listener.onCancelClicked()
            },
            hideWhenDisabled = true
        )
    }

    internal fun setState(state: State) {
        when (state) {
            State.AwaitingCardDetails -> transitionToAwaitingCardDetails()
            State.AwaitingGooglePayDetails -> transitionToAwaitingGooglePayDetails()
            State.ReadyToProcess -> transitionToReadyToProcess()
            State.Processing -> transitionToProcessing()
        }
    }

    private fun transitionToAwaitingCardDetails() {
        state = State.AwaitingCardDetails
        payButton.update(enabled = false)
        cancelButton.update(enabled = true)
        payButton.setText(determinePayButtonTitle())
    }

    private fun transitionToAwaitingGooglePayDetails() {
        state = State.AwaitingGooglePayDetails
        toggleButtons(enabled = false)
        payButton.setText(determinePayButtonTitle())
    }

    private fun transitionToReadyToProcess() {
        state = State.ReadyToProcess
        toggleButtons(enabled = true)
        payButton.setText(determinePayButtonTitle())
    }

    private fun transitionToProcessing() {
        state = State.Processing
        toggleButtons(enabled = false)
        payButton.setText(determinePayButtonTitle())
    }

    private fun toggleButtons(enabled: Boolean) {
        payButton.update(enabled)
        cancelButton.update(enabled)
    }

    private fun determinePayButtonTitle(): String = when {
        state == State.Processing -> context.getString(R.string.ryft_processing)
        usage == RyftDropInUsage.SetupCard -> context.getString(R.string.ryft_save_card)
        else -> payButtonTitleOverride?.ifBlank { null } ?: context.getString(R.string.ryft_pay)
    }

    internal enum class State {
        AwaitingCardDetails,
        AwaitingGooglePayDetails,
        ReadyToProcess,
        Processing
    }
}
