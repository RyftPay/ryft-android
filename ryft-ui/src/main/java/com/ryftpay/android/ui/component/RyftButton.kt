package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.ryftpay.android.ui.extension.setOnSingleClickListener
import com.ryftpay.ui.R

internal class RyftButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private lateinit var title: TextView
    private val defaultOpacity = 1F
    private var opacityWhenDisabled: Float = defaultOpacity
    private var hideWhenDisabled: Boolean = false

    internal fun initialise(
        text: String,
        enabled: Boolean,
        clickListener: (View) -> Unit,
        opacityWhenDisabled: Float = 1F,
        hideWhenDisabled: Boolean = false
    ) {
        this.title = findViewById(R.id.text_ryft_button)
        this.opacityWhenDisabled = opacityWhenDisabled
        this.hideWhenDisabled = hideWhenDisabled
        setOnSingleClickListener(clickListener)
        setText(text)
        update(enabled)
    }

    internal fun setText(text: String) {
        title.text = text
    }

    internal fun update(enabled: Boolean) {
        isEnabled = enabled
        isClickable = enabled
        if (hideWhenDisabled) {
            visibility = if (enabled) View.VISIBLE else View.GONE
        }
        alpha = if (enabled) defaultOpacity else opacityWhenDisabled
    }
}
