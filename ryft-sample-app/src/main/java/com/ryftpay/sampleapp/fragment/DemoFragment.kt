package com.ryftpay.sampleapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.RequiredAction
import com.ryftpay.android.core.model.payment.RequiredActionType
import com.ryftpay.android.ui.dropin.DefaultRyftDropIn
import com.ryftpay.android.ui.dropin.RyftDropIn
import com.ryftpay.android.ui.dropin.RyftDropInConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInDisplayConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInGooglePayConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInResultListener
import com.ryftpay.android.ui.dropin.RyftDropInUsage
import com.ryftpay.android.ui.dropin.RyftPaymentResult
import com.ryftpay.android.ui.dropin.threeds.DefaultRyftRequiredActionComponent
import com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionComponent
import com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionResult
import com.ryftpay.android.ui.dropin.threeds.RyftRequiredActionResultListener
import com.ryftpay.sampleapp.R
import org.json.JSONObject

class DemoFragment : Fragment(), RyftDropInResultListener, RyftRequiredActionResultListener {

    // Sample app variables
    private lateinit var publicApiKeyInput: EditText
    private lateinit var clientSecretInput: EditText
    private lateinit var subAccountIdInput: EditText
    private lateinit var requiredActionJsonInput: EditText
    private var selectedUsage: RyftDropInUsage = RyftDropInUsage.Payment
    private lateinit var paymentButton: Button
    private lateinit var setupCardButton: Button
    private lateinit var handleRequiredActionButton: Button

    // Declare RyftDropIn
    private lateinit var ryftDropIn: RyftDropIn

    // Declare RyftRequiredActionComponent
    private lateinit var ryftRequiredActionComponent: RyftRequiredActionComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Instantiate DefaultRyftDropIn in onCreate()
        ryftDropIn = DefaultRyftDropIn(
            fragment = this,
            listener = this
        )
        // Instantiate DefaultRyftRequiredActionComponent in onCreate()
        ryftRequiredActionComponent = DefaultRyftRequiredActionComponent(
            fragment = this,
            listener = this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instantiate sample app variables
        publicApiKeyInput = view.findViewById(R.id.input_public_api_key)
        clientSecretInput = view.findViewById(R.id.input_client_secret)
        subAccountIdInput = view.findViewById(R.id.input_sub_account_id)
        requiredActionJsonInput = view.findViewById(R.id.input_required_action_json)
        paymentButton = view.findViewById(R.id.button_payment)
        setupCardButton = view.findViewById(R.id.button_setup_card)
        handleRequiredActionButton = view.findViewById(R.id.button_handle_required_action)

        // Load Ryft payment sheet when payment is clicked
        paymentButton.setOnClickListener {
            selectedUsage = RyftDropInUsage.Payment
            showDropIn()
        }

        // Load Ryft payment sheet when setup card is clicked
        setupCardButton.setOnClickListener {
            selectedUsage = RyftDropInUsage.SetupCard
            showDropIn()
        }

        handleRequiredActionButton.setOnClickListener {
            handleRequiredAction()
        }
    }

    // Handle RyftPaymentResult
    override fun onPaymentResult(result: RyftPaymentResult) = when (result) {
        is RyftPaymentResult.Approved -> {
            displayInformativeAlert(
                title = when (selectedUsage) {
                    RyftDropInUsage.Payment -> "Payment successful"
                    RyftDropInUsage.SetupCard -> "Card saved successfully"
                },
                message = "Hooray!"
            )
            clientSecretInput.text.clear()
        }
        is RyftPaymentResult.Failed -> {
            displayErrorAlert(
                errorMessage = result.error.displayError
            ) {
                showDropIn()
            }
        }
        is RyftPaymentResult.Cancelled -> {
            when (selectedUsage) {
                RyftDropInUsage.Payment -> displayInformativeAlert(
                    title = "Payment cancelled",
                    message = "You cancelled the payment"
                )
                RyftDropInUsage.SetupCard -> displayInformativeAlert(
                    title = "Card setup cancelled",
                    message = "You cancelled the card setup"
                )
            }
        }
    }

    override fun onRequiredActionResult(result: RyftRequiredActionResult) = when (result) {
        is RyftRequiredActionResult.Success -> {
            displayInformativeAlert(
                title = "Required Action successful",
                message = "Hooray! Status: ${result.paymentSession.status}"
            )
            clientSecretInput.text.clear()
            requiredActionJsonInput.text.clear()
        }
        is RyftRequiredActionResult.Error -> {
            displayErrorAlert(
                errorMessage = result.error.displayError
            ) {
                handleRequiredAction()
            }
        }
    }

    private fun showDropIn() {
        // Show drop in with configuration
        ryftDropIn.show(
            RyftDropInConfiguration(
                clientSecret = clientSecretInput.text.toString(),
                publicApiKey = RyftPublicApiKey(publicApiKeyInput.text.toString()),
                subAccountId = parseSubAccountId(),
                display = RyftDropInDisplayConfiguration(
                    payButtonTitle = "Pay Demo",
                    usage = selectedUsage
                ),
                googlePayConfiguration = RyftDropInGooglePayConfiguration(
                    merchantName = DEMO_MERCHANT_NAME,
                    merchantCountryCode = DEMO_MERCHANT_COUNTRY_CODE
                )
            )
        )
    }

    private fun handleRequiredAction() {
        val subAccountId = parseSubAccountId()
        val configuration = if (subAccountId != null) {
            RyftRequiredActionComponent.Configuration.subAccountPayment(
                clientSecret = clientSecretInput.text.toString(),
                publicApiKey = RyftPublicApiKey(publicApiKeyInput.text.toString()),
                subAccountId = subAccountId
            )
        } else {
            RyftRequiredActionComponent.Configuration.standardAccountPayment(
                clientSecret = clientSecretInput.text.toString(),
                publicApiKey = RyftPublicApiKey(publicApiKeyInput.text.toString())
            )
        }
        ryftRequiredActionComponent.handle(
            configuration,
            parseRequiredActionJson()
        )
    }

    private fun displayInformativeAlert(
        title: String,
        message: String
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun displayErrorAlert(
        errorMessage: String,
        function: () -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error taking payment")
            .setMessage(errorMessage)
            .setPositiveButton("Try again") { _, _ ->
                function()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun parseSubAccountId(): String? = if (subAccountIdInput.text.any()) {
        subAccountIdInput.text.toString()
    } else {
        null
    }

    private fun parseRequiredActionJson(): RequiredAction {
        val requiredActionJson = JSONObject(requiredActionJsonInput.text.toString())
        val identifyJson = requiredActionJson.optJSONObject("identify")
        val identify = if (identifyJson != null) {
            IdentifyAction(
                sessionId = identifyJson.getString("sessionId"),
                sessionSecret = identifyJson.getString("sessionSecret"),
                scheme = identifyJson.getString("scheme"),
                paymentMethodId = identifyJson.getString("paymentMethodId")
            )
        } else {
            null
        }
        return RequiredAction(
            type = RequiredActionType.valueOf(requiredActionJson.getString("type")),
            url = requiredActionJson.optString("url").ifBlank { null },
            identify = identify
        )
    }

    companion object {
        // Sample app constants
        private const val DEMO_MERCHANT_NAME = "Ryft"
        private const val DEMO_MERCHANT_COUNTRY_CODE = "GB"

        fun newInstance(): DemoFragment = DemoFragment()
    }
}
