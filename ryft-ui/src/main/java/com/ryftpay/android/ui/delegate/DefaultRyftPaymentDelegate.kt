package com.ryftpay.android.ui.delegate

import android.view.View
import com.ryftpay.android.ui.component.RyftPaymentFormBody
import com.ryftpay.android.ui.component.RyftPaymentFormCardOnlyHeader
import com.ryftpay.android.ui.component.RyftPaymentFormFooter
import com.ryftpay.android.ui.component.RyftPaymentFormGooglePayHeader
import com.ryftpay.android.ui.listener.RyftPaymentFormBodyListener
import com.ryftpay.android.ui.listener.RyftPaymentFormFooterListener
import com.ryftpay.android.ui.listener.RyftPaymentFormGooglePayHeaderListener
import com.ryftpay.android.ui.listener.RyftPaymentFormListener
import com.ryftpay.ui.R

internal class DefaultRyftPaymentDelegate(
    private val listener: RyftPaymentFormListener
) : RyftPaymentDelegate,
    RyftPaymentFormBodyListener,
    RyftPaymentFormFooterListener,
    RyftPaymentFormGooglePayHeaderListener {

    private lateinit var cardOnlyHeader: RyftPaymentFormCardOnlyHeader
    private lateinit var googlePayHeader: RyftPaymentFormGooglePayHeader
    private lateinit var body: RyftPaymentFormBody
    private lateinit var footer: RyftPaymentFormFooter

    override fun onViewCreated(root: View, showGooglePay: Boolean) {
        cardOnlyHeader = root.findViewById(R.id.partial_ryft_payment_form_card_only_header)
        cardOnlyHeader.initialise()
        googlePayHeader = root.findViewById(R.id.partial_ryft_payment_form_googlepay_header)
        googlePayHeader.initialise(listener = this)
        body = root.findViewById(R.id.partial_ryft_payment_form_body)
        body.initialise(listener = this)
        footer = root.findViewById(R.id.partial_ryft_payment_form_footer)
        footer.initialise(listener = this)
        setHeaderVisibility(showGooglePay)
    }

    override fun onGooglePayPaymentProcessing() {
        toggleFormSubmission(
            enabled = false,
            footerState = RyftPaymentFormFooter.State.TakingPayment
        )
    }

    override fun onGooglePayFailedOrCancelled() {
        val footerState = if (body.currentCard.valid) {
            RyftPaymentFormFooter.State.ReadyForCardPayment
        } else {
            RyftPaymentFormFooter.State.AwaitingCardDetails
        }
        toggleFormSubmission(
            enabled = true,
            footerState = footerState
        )
    }

    override fun onReadyForCardPayment() {
        footer.setState(RyftPaymentFormFooter.State.ReadyForCardPayment)
    }

    override fun onAwaitingCardDetails() {
        footer.setState(RyftPaymentFormFooter.State.AwaitingCardDetails)
    }

    override fun onPayClicked() {
        if (!body.currentCard.valid) {
            return
        }
        toggleFormSubmission(
            enabled = false,
            footerState = RyftPaymentFormFooter.State.TakingPayment
        )
        listener.onPayByCard(body.currentCard)
    }

    override fun onCancelClicked() {
        listener.onCancel()
    }

    override fun onGooglePayClicked() {
        toggleFormSubmission(
            enabled = false,
            footerState = RyftPaymentFormFooter.State.AwaitingGooglePayDetails
        )
        listener.onPayByGooglePay()
    }

    private fun setHeaderVisibility(showGooglePay: Boolean) {
        cardOnlyHeader.visibility = if (showGooglePay) View.GONE else View.VISIBLE
        googlePayHeader.visibility = if (showGooglePay) View.VISIBLE else View.GONE
    }

    private fun toggleFormSubmission(
        enabled: Boolean,
        footerState: RyftPaymentFormFooter.State
    ) {
        googlePayHeader.toggleGooglePayButton(enabled)
        body.toggleInput(enabled)
        footer.setState(footerState)
    }
}
