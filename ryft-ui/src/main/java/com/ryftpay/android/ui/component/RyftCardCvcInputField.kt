package com.ryftpay.android.ui.component

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import com.ryftpay.android.ui.component.watcher.AfterTextWatcherWithAction
import com.ryftpay.android.ui.component.watcher.TextWatcherAction
import com.ryftpay.android.ui.extension.addOrReplaceFilter
import com.ryftpay.android.ui.listener.RyftCardCvcInputListener
import com.ryftpay.android.ui.model.RyftCardCvc
import com.ryftpay.android.ui.model.RyftCardType
import com.ryftpay.ui.R

internal class RyftCardCvcInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RyftInputField(context, attrs, R.style.RyftCardCvcInputField) {

    internal var currentCardType = RyftCardType.Unknown

    internal fun initialise(listener: RyftCardCvcInputListener) {
        onFocusChangeListener = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE)
        }
        this.addOrReplaceFilter(InputFilter.LengthFilter(currentCardType.cvcLength))
        this.addTextChangedListener(object : AfterTextWatcherWithAction() {
            override fun afterTextChanged(text: String?, action: TextWatcherAction) {
                val cvc = RyftCardCvc.from(text, currentCardType)
                updateValidationState(cvc.validationState)
                listener.onCardCvcChanged(cvc)
            }
        })
    }
}
