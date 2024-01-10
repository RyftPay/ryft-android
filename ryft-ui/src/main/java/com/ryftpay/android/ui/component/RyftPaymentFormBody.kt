package com.ryftpay.android.ui.component

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.ryftpay.android.ui.dropin.RyftDropInUsage
import com.ryftpay.android.ui.extension.addOrReplaceFilter
import com.ryftpay.android.ui.listener.RyftCardCvcInputListener
import com.ryftpay.android.ui.listener.RyftCardExpiryDateInputListener
import com.ryftpay.android.ui.listener.RyftCardNameInputListener
import com.ryftpay.android.ui.listener.RyftCardNumberInputListener
import com.ryftpay.android.ui.listener.RyftCheckBoxListener
import com.ryftpay.android.ui.listener.RyftPaymentFormBodyListener
import com.ryftpay.android.ui.model.RyftCard
import com.ryftpay.android.ui.model.RyftCardCvc
import com.ryftpay.android.ui.model.RyftCardExpiryDate
import com.ryftpay.android.ui.model.RyftCardName
import com.ryftpay.android.ui.model.RyftCardNumber
import com.ryftpay.android.ui.model.RyftCardOptions
import com.ryftpay.android.ui.model.RyftCardType
import com.ryftpay.android.ui.model.ValidationState
import com.ryftpay.ui.R

internal class RyftPaymentFormBody @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs),
    RyftCardNumberInputListener,
    RyftCardNameInputListener,
    RyftCardExpiryDateInputListener,
    RyftCardCvcInputListener,
    RyftCheckBoxListener {

    internal var currentCardOptions = RyftCardOptions.Default
    internal lateinit var currentCard: RyftCard

    private lateinit var cardNumberField: RyftCardNumberInputField
    private lateinit var cardNameRow: LinearLayout
    private lateinit var cardNameField: RyftCardNameInputField
    private lateinit var cardExpiryDateField: RyftCardExpiryDateInputField
    private lateinit var cardCvcField: RyftCardCvcInputField
    private lateinit var saveCardCheckBox: RyftCheckBox
    private lateinit var saveCardDisclaimer: TextView
    private lateinit var listener: RyftPaymentFormBodyListener

    internal fun initialise(
        usage: RyftDropInUsage,
        collectNameOnCard: Boolean,
        listener: RyftPaymentFormBodyListener
    ) {
        this.listener = listener

        currentCard = RyftCard.incomplete(collectNameOnCard)

        cardNumberField = findViewById(R.id.input_field_ryft_card_number)
        cardNumberField.initialise(
            currentCard.number,
            listener = this
        )
        cardNameField = findViewById(R.id.input_field_ryft_card_name)
        cardNameField.initialise(
            listener = this
        )
        cardNameRow = findViewById(R.id.row_ryft_card_name)
        cardNameRow.visibility = if (collectNameOnCard) View.VISIBLE else View.GONE
        cardExpiryDateField = findViewById(R.id.input_field_ryft_card_expiry_date)
        cardExpiryDateField.initialise(
            listener = this
        )
        cardCvcField = findViewById(R.id.input_field_ryft_card_cvc)
        cardCvcField.initialise(
            listener = this
        )
        saveCardCheckBox = findViewById(R.id.check_box_ryft_save_card)
        saveCardCheckBox.initialise(
            text = context.getString(R.string.ryft_save_card_check_box),
            listener = this
        )
        saveCardCheckBox.visibility = if (usage == RyftDropInUsage.Payment) View.VISIBLE else View.GONE
        saveCardDisclaimer = findViewById(R.id.text_ryft_save_card_disclaimer)
        saveCardDisclaimer.visibility = if (usage == RyftDropInUsage.SetupCard) View.VISIBLE else View.GONE

        if (collectNameOnCard) {
            cardNameField.requestFocus()
        } else {
            cardNumberField.requestFocus()
        }
        toggleInput(enabled = true)
    }

    internal fun toggleInput(enabled: Boolean) {
        cardNameField.isEnabled = enabled
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

    override fun onCardNameChanged(
        name: RyftCardName,
        validationState: ValidationState
    ) {
        onCardChanged(currentCard.withName(name))
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

    override fun onCheckBoxChanged(checked: Boolean) =
        onCardOptionsChanged(currentCardOptions.withSaveForFuture(saveForFuture = checked))

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

    private fun onCardOptionsChanged(options: RyftCardOptions) {
        currentCardOptions = options
    }

    private fun onCardTypeChanged(cardType: RyftCardType) {
        cardNumberField.updateIcon(cardType.iconDrawableId)
        cardNumberField.addOrReplaceFilter(InputFilter.LengthFilter(cardType.maxFormattedCardLength))
        cardCvcField.addOrReplaceFilter(InputFilter.LengthFilter(cardType.cvcLength))
        cardCvcField.currentCardType = cardType
    }
}
