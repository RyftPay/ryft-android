package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.ryftpay.android.ui.extension.setOnSingleClickListener
import com.ryftpay.android.ui.listener.RyftPaymentFormFooterListener
import com.ryftpay.ui.R

internal class RyftPaymentFormFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var payButton: RyftButton
    private lateinit var cancelButton: RyftButton
    private lateinit var state: State

    internal fun initialise(listener: RyftPaymentFormFooterListener) {
        payButton = findViewById(R.id.button_ryft_pay)
        cancelButton = findViewById(R.id.button_ryft_cancel)

        payButton.initialise(context.getString(R.string.ryft_pay))
        cancelButton.initialise(context.getString(R.string.ryft_cancel))

        payButton.setOnSingleClickListener {
            toggleButtons(enabled = false)
            listener.onPayClicked()
        }
        cancelButton.setOnSingleClickListener {
            toggleButtons(enabled = false)
            listener.onCancelClicked()
        }
        transitionToAwaitingCardDetails()
    }

    internal fun setState(state: State) {
        when (state) {
            State.AwaitingCardDetails -> transitionToAwaitingCardDetails()
            State.AwaitingGooglePayDetails -> transitionToAwaitingGooglePayDetails()
            State.ReadyForCardPayment -> transitionToReadyForCardPayment()
            State.TakingPayment -> transitionToTakingPayment()
        }
    }

    private fun transitionToAwaitingCardDetails() {
        togglePayButton(enabled = false)
        toggleCancelButton(enabled = true)
        payButton.setText(context.getString(R.string.ryft_pay))
        state = State.AwaitingCardDetails
    }

    private fun transitionToAwaitingGooglePayDetails() {
        toggleButtons(enabled = false)
        payButton.setText(context.getString(R.string.ryft_pay))
        state = State.AwaitingGooglePayDetails
    }

    private fun transitionToReadyForCardPayment() {
        toggleButtons(enabled = true)
        payButton.setText(context.getString(R.string.ryft_pay))
        state = State.ReadyForCardPayment
    }

    private fun transitionToTakingPayment() {
        toggleButtons(enabled = false)
        payButton.setText(context.getString(R.string.ryft_taking_payment))
        state = State.TakingPayment
    }

    private fun toggleButtons(enabled: Boolean) {
        togglePayButton(enabled)
        toggleCancelButton(enabled)
    }

    private fun togglePayButton(enabled: Boolean) {
        payButton.isEnabled = enabled
        payButton.isClickable = enabled
        payButton.alpha = if (enabled) 1F else 0.7F
    }

    private fun toggleCancelButton(enabled: Boolean) {
        cancelButton.isEnabled = enabled
        cancelButton.isClickable = enabled
        cancelButton.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    internal enum class State {
        AwaitingCardDetails,
        AwaitingGooglePayDetails,
        ReadyForCardPayment,
        TakingPayment
    }
}
