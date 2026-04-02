package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import com.ryftpay.android.ui.listener.RyftPaymentFormGooglePayHeaderListener
import com.ryftpay.ui.R
import org.json.JSONArray
import org.json.JSONObject

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
            JSONObject().apply {
                put("type", "CARD")
                put(
                    "parameters",
                    JSONObject().apply {
                        put(
                            "allowedAuthMethods",
                            JSONArray().apply {
                                put("PAN_ONLY")
                                put("CRYPTOGRAM_3DS")
                            }
                        )
                        put(
                            "allowedCardNetworks",
                            JSONArray().apply {
                                put("VISA")
                                put("MASTERCARD")
                                put("AMEX")
                                put("DISCOVER")
                            }
                        )
                    }
                )
            }
        )

        try {
            googlePayButton.initialize(
                ButtonOptions.newBuilder()
                    .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
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
            toggleGooglePayButton(enabled = false)
            listener.onGooglePayClicked()
        }
    }

    internal fun toggleGooglePayButton(enabled: Boolean) {
        if (enabled) {
            googlePayButton.visibility = View.VISIBLE
        } else {
            googlePayButton.visibility = View.GONE
        }
    }
}
