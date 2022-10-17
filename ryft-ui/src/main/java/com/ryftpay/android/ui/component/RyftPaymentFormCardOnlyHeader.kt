package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.ryftpay.android.ui.dropin.RyftDropInUsage
import com.ryftpay.ui.R

internal class RyftPaymentFormCardOnlyHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var title: TextView

    internal fun initialise(usage: RyftDropInUsage) {
        title = findViewById(R.id.text_ryft_payment_form_card_only_header)
        title.text = when (usage) {
            RyftDropInUsage.Payment -> context.getString(R.string.ryft_payment_form_card_only_header_title)
            RyftDropInUsage.SetupCard -> context.getString(R.string.ryft_save_card_form_title)
        }
    }
}
