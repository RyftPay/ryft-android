package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import com.ryftpay.android.ui.listener.RyftPaymentFormGooglePayHeaderListener
import com.ryftpay.android.ui.model.googlepay.CardPaymentMethod
import com.ryftpay.ui.R
import org.json.JSONArray

internal class RyftPaymentFormGooglePayHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var googlePayButton: PayButton

    internal fun initialise(
        listener: RyftPaymentFormGooglePayHeaderListener
    ) {
        googlePayButton = findViewById(R.id.button_ryft_googlepay)

        val allowedPaymentMethods = JSONArray().put(
            CardPaymentMethod().toApiV2RequestJson(
                billingAddressRequired = false,
                tokenizationSpecification = null
            )
        )

        try {
            googlePayButton.initialize(
                ButtonOptions.newBuilder()
                    .setButtonType(ButtonConstants.ButtonType.PAY)
                    .setAllowedPaymentMethods(allowedPaymentMethods.toString())
                    .build()
            )
        } catch (e: Exception) {
            // Handle initialization error - keep button hidden
            googlePayButton.visibility = View.GONE
            return
        }

        // Set up click listener and ensure visibility
        visibility = View.VISIBLE
        googlePayButton.visibility = View.VISIBLE

        googlePayButton.setOnClickListener {
            listener.onGooglePayClicked()
        }
    }
}
