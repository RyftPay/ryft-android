package com.ryftpay.android.ui.component

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import com.ryftpay.android.ui.component.watcher.AfterTextWatcherWithAction
import com.ryftpay.android.ui.component.watcher.TextWatcherAction
import com.ryftpay.android.ui.extension.addOrReplaceFilter
import com.ryftpay.android.ui.extension.setText
import com.ryftpay.android.ui.listener.RyftCardNumberInputListener
import com.ryftpay.android.ui.model.RyftCardNumber
import com.ryftpay.ui.R

internal class RyftCardNumberInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RyftInputField(context, attrs, R.style.RyftCardNumberInputField) {

    internal fun initialise(
        initialCardNumber: RyftCardNumber,
        listener: RyftCardNumberInputListener
    ) {
        onFocusChangeListener = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER)
        }
        this.addOrReplaceFilter(InputFilter.LengthFilter(initialCardNumber.derivedType.maxFormattedCardLength))
        this.addTextChangedListener(object : AfterTextWatcherWithAction() {
            override fun afterTextChanged(text: String?, action: TextWatcherAction) {
                val cardNumber = RyftCardNumber.from(text)
                val formattedCardNumber = cardNumber.formatted(
                    removeTrailingSeparator = currentAction == TextWatcherAction.Delete
                )
                if (formattedCardNumber != text) {
                    setText(editText = this@RyftCardNumberInputField, formattedCardNumber)
                }
                updateValidationState(cardNumber.validationState)
                listener.onCardNumberChanged(cardNumber, cardNumber.validationState)
            }
        })
    }
}
