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
import com.ryftpay.android.ui.listener.RyftCardExpiryDateInputListener
import com.ryftpay.android.ui.model.RyftCardExpiryDate
import com.ryftpay.ui.R

internal class RyftCardExpiryDateInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RyftInputField(context, attrs, R.style.RyftCardExpiryDateInputField) {

    internal fun initialise(listener: RyftCardExpiryDateInputListener) {
        onFocusChangeListener = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE)
        }
        this.addOrReplaceFilter(InputFilter.LengthFilter(RyftCardExpiryDate.MAX_FORMATTED_LENGTH))
        this.addTextChangedListener(object : AfterTextWatcherWithAction() {
            override fun afterTextChanged(text: String?, action: TextWatcherAction) {
                val expiryDate = RyftCardExpiryDate.from(text)
                val formattedExpiryDate = expiryDate.formatted(
                    removeTrailingSeparator = action == TextWatcherAction.Delete
                )
                if (formattedExpiryDate != text) {
                    setText(editText = this@RyftCardExpiryDateInputField, formattedExpiryDate)
                }
                updateValidationState(expiryDate.validationState)
                listener.onCardExpiryDateChanged(expiryDate, expiryDate.validationState)
            }
        })
    }
}
