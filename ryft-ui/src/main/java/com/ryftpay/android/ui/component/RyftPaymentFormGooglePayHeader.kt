package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.ryftpay.android.ui.extension.setOnSingleClickListener
import com.ryftpay.android.ui.listener.RyftPaymentFormGooglePayHeaderListener
import com.ryftpay.ui.R

internal class RyftPaymentFormGooglePayHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var googlePayButton: RelativeLayout

    internal fun initialise(
        listener: RyftPaymentFormGooglePayHeaderListener
    ) {
        googlePayButton = findViewById(R.id.button_ryft_googlepay)
        toggleGooglePayButton(enabled = true)
        googlePayButton.setOnSingleClickListener {
            toggleGooglePayButton(enabled = false)
            listener.onGooglePayClicked()
        }
    }

    internal fun toggleGooglePayButton(enabled: Boolean) {
        googlePayButton.isEnabled = enabled
        googlePayButton.isClickable = enabled
    }
}
