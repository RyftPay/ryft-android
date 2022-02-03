package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import com.ryftpay.ui.R

internal class RyftButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private lateinit var title: TextView

    internal fun initialise(text: String) {
        title = findViewById(R.id.text_ryft_button)
        setText(text)
    }

    internal fun setText(text: String) {
        title.text = text
    }
}
