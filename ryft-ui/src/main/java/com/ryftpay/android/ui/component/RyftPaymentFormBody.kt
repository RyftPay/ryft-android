package com.ryftpay.android.ui.component

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ryftpay.android.ui.extension.addOrReplaceFilter
import com.ryftpay.android.ui.listener.RyftCardCvcInputListener
import com.ryftpay.android.ui.listener.RyftCardExpiryDateInputListener
import com.ryftpay.android.ui.listener.RyftCardNumberInputListener
import com.ryftpay.android.ui.listener.RyftPaymentFormBodyListener
import com.ryftpay.android.ui.model.RyftCard
import com.ryftpay.android.ui.model.RyftCardCvc
import com.ryftpay.android.ui.model.RyftCardExpiryDate
import com.ryftpay.android.ui.model.RyftCardNumber
import com.ryftpay.android.ui.model.RyftCardType
import com.ryftpay.android.ui.model.ValidationState
import com.ryftpay.ui.R

internal class RyftPaymentFormBody @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs),
    RyftCardNumberInputListener,
    RyftCardExpiryDateInputListener,
    RyftCardCvcInputListener {

    internal var currentCard = RyftCard.Incomplete

    private lateinit var cardNumberField: RyftCardNumberInputField
    private lateinit var cardExpiryDateField: RyftCardExpiryDateInputField
    private lateinit var cardCvcField: RyftCardCvcInputField
    private lateinit var listener: RyftPaymentFormBodyListener

    internal fun initialise(listener: RyftPaymentFormBodyListener) {
        this.listener = listener
        cardNumberField = findViewById(R.id.input_field_ryft_card_number)
        cardNumberField.initialise(
            currentCard.number,
            listener = this
        )
        cardExpiryDateField = findViewById(R.id.input_field_ryft_card_expiry_date)
        cardExpiryDateField.initialise(
            listener = this
        )
        cardCvcField = findViewById(R.id.input_field_ryft_card_cvc)
        cardCvcField.initialise(
            listener = this
        )

        cardNumberField.requestFocus()
        toggleInput(enabled = true)
    }

    internal fun toggleInput(enabled: Boolean) {
        cardNumberField.isEnabled = enabled
        cardExpiryDateField.isEnabled = enabled
        cardCvcField.isEnabled = enabled
    }

    override fun onCardNumberChanged(
        cardNumber: RyftCardNumber,
        validationState: ValidationState
    ) {
        onCardChanged(currentCard.withCardNumber(cardNumber))
        if (validationState == ValidationState.Valid &&
            currentCard.expiryDate.validationState != ValidationState.Valid
        ) {
            cardExpiryDateField.requestFocus()
        }
    }

    override fun onCardExpiryDateChanged(
        expiryDate: RyftCardExpiryDate,
        validationState: ValidationState
    ) {
        onCardChanged(currentCard.withExpiryDate(expiryDate))
        if (validationState == ValidationState.Valid &&
            currentCard.cvc.validationState != ValidationState.Valid
        ) {
            cardCvcField.requestFocus()
        }
    }

    override fun onCardCvcChanged(cvc: RyftCardCvc) {
        onCardChanged(currentCard.withCvc(cvc))
        if (cvc.validationState == ValidationState.Valid) {
            cardCvcField.clearFocus()
        }
    }

    private fun onCardChanged(newCard: RyftCard) {
        if (newCard.type != currentCard.type) {
            onCardTypeChanged(newCard.type)
        }
        if (newCard.valid && !currentCard.valid) {
            listener.onReadyForCardPayment()
        }
        if (!newCard.valid && currentCard.valid) {
            listener.onAwaitingCardDetails()
        }
        currentCard = newCard
    }

    private fun onCardTypeChanged(cardType: RyftCardType) {
        cardNumberField.updateIcon(cardType.iconDrawableId)
        cardNumberField.addOrReplaceFilter(InputFilter.LengthFilter(cardType.maxFormattedCardLength))
        cardCvcField.addOrReplaceFilter(InputFilter.LengthFilter(cardType.cvcLength))
        cardCvcField.currentCardType = cardType
    }
}
