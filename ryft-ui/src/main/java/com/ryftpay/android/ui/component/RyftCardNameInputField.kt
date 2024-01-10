package com.ryftpay.android.ui.component

import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import com.ryftpay.android.ui.component.watcher.AfterTextWatcherWithAction
import com.ryftpay.android.ui.component.watcher.TextWatcherAction
import com.ryftpay.android.ui.extension.addOrReplaceFilter
import com.ryftpay.android.ui.listener.RyftCardNameInputListener
import com.ryftpay.android.ui.model.RyftCardName
import com.ryftpay.ui.R

internal class RyftCardNameInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RyftInputField(context, attrs, R.style.RyftCardNumberInputField) {

    internal fun initialise(
        listener: RyftCardNameInputListener
    ) {
        onFocusChangeListener = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.setAutofillHints(View.AUTOFILL_HINT_NAME)
        }
        this.addOrReplaceFilter(InputFilter.LengthFilter(RyftCardName.MAX_LENGTH))
        this.addTextChangedListener(object : AfterTextWatcherWithAction() {
            override fun afterTextChanged(text: String?, action: TextWatcherAction) {
                val name = RyftCardName.from(text)
                updateValidationState(name.validationState)
                listener.onCardNameChanged(name, name.validationState)
            }
        })
    }
}
