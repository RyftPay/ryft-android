package com.ryftpay.android.ui.component

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.ryftpay.android.ui.model.ValidationState
import com.ryftpay.ui.R

internal open class RyftInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr), View.OnFocusChangeListener {

    init {
        isFocusableInTouchMode = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusable = View.FOCUSABLE
        }
    }

    private var validationState = ValidationState.Incomplete

    internal fun updateIcon(drawableId: Int) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawableId, 0)
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        updateBorderStyle(p1, validationState)
    }

    protected fun updateValidationState(
        newValidationState: ValidationState
    ) {
        updateBorderStyle(hasFocus = true, newValidationState)
        validationState = newValidationState
    }

    private fun updateBorderStyle(
        hasFocus: Boolean,
        validationState: ValidationState
    ) {
        when (validationState) {
            ValidationState.Invalid ->
                this.setBackgroundResource(R.drawable.bg_ryft_input_field_error)
            else -> {
                this.setBackgroundResource(
                    if (hasFocus) {
                        R.drawable.bg_ryft_input_field_selected
                    } else {
                        R.drawable.bg_ryft_input_field
                    }
                )
            }
        }
    }
}
