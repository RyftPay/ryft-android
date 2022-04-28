package com.ryftpay.android.ui.delegate

import android.view.View
import com.ryftpay.android.ui.component.RyftPaymentFormBody
import com.ryftpay.android.ui.component.RyftPaymentFormCardOnlyHeader
import com.ryftpay.android.ui.component.RyftPaymentFormFooter
import com.ryftpay.android.ui.listener.RyftPaymentFormBodyListener
import com.ryftpay.android.ui.listener.RyftPaymentFormFooterListener
import com.ryftpay.android.ui.listener.RyftPaymentFormListener
import com.ryftpay.ui.R

internal class DefaultRyftPaymentDelegate(
    private val listener: RyftPaymentFormListener
) : RyftPaymentDelegate,
    RyftPaymentFormBodyListener,
    RyftPaymentFormFooterListener {

    private lateinit var cardOnlyHeader: RyftPaymentFormCardOnlyHeader
    private lateinit var body: RyftPaymentFormBody
    private lateinit var footer: RyftPaymentFormFooter

    override fun onViewCreated(root: View) {
        cardOnlyHeader = root.findViewById(R.id.partial_ryft_payment_form_card_only_header)
        cardOnlyHeader.initialise()
        body = root.findViewById(R.id.partial_ryft_payment_form_body)
        body.initialise(listener = this)
        footer = root.findViewById(R.id.partial_ryft_payment_form_footer)
        footer.initialise(listener = this)
    }

    override fun onCardReadyForPayment() {
        footer.setState(RyftPaymentFormFooter.State.ReadyForPayment)
    }

    override fun onAwaitingCardDetails() {
        footer.setState(RyftPaymentFormFooter.State.AwaitingPaymentDetails)
    }

    override fun onPayClicked() {
        if (!body.currentCard.valid) {
            return
        }
        body.toggleInput(enabled = false)
        footer.setState(RyftPaymentFormFooter.State.TakingPayment)
        listener.onPay(body.currentCard)
    }

    override fun onCancelClicked() {
        listener.onCancel()
    }
}
