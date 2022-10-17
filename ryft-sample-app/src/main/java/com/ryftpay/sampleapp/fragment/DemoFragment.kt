package com.ryftpay.sampleapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.ui.dropin.DefaultRyftDropIn
import com.ryftpay.android.ui.dropin.RyftDropIn
import com.ryftpay.android.ui.dropin.RyftDropInConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInDisplayConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInGooglePayConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInResultListener
import com.ryftpay.android.ui.dropin.RyftDropInUsage
import com.ryftpay.android.ui.dropin.RyftPaymentResult
import com.ryftpay.sampleapp.R
import kotlinx.parcelize.Parcelize

class DemoFragment : Fragment(), RyftDropInResultListener {

    // Sample app variables
    private lateinit var clientSecretInput: EditText
    private lateinit var subAccountIdInput: EditText
    private lateinit var usageInput: Spinner
    private lateinit var paymentButton: Button

    // Declare RyftDropIn
    private lateinit var ryftDropIn: RyftDropIn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val input: Arguments = arguments?.getParcelable(ARGUMENTS_BUNDLE_KEY)
            ?: throw IllegalArgumentException("No arguments provided to fragment")
        // Instantiate DefaultRyftDropIn in onCreate()
        ryftDropIn = DefaultRyftDropIn(
            fragment = this,
            listener = this,
            RyftPublicApiKey(input.publicApiKey)
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
        clientSecretInput = view.findViewById(R.id.input_client_secret)
        subAccountIdInput = view.findViewById(R.id.input_sub_account_id)
        usageInput = view.findViewById(R.id.input_usage)
        val usages = RyftDropInUsage.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, usages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        usageInput.adapter = adapter
        usageInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                selectedItem.let {
                    val selectedUsage = RyftDropInUsage.valueOf(it)
                    paymentButton.text = when (selectedUsage) {
                        RyftDropInUsage.Payment -> requireContext().getString(R.string.button_payment)
                        RyftDropInUsage.SetupCard -> requireContext().getString(R.string.button_setup_card)
                    }
                }
            }
        }
        paymentButton = view.findViewById(R.id.button_payment)

        // Load Ryft payment sheet when payment is clicked
        paymentButton.setOnClickListener {
            showDropIn()
        }
    }

    // Handle RyftPaymentResult
    override fun onPaymentResult(result: RyftPaymentResult) {
        val selectedUsage = RyftDropInUsage.valueOf(usageInput.selectedItem.toString())
        return when (result) {
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
                )
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
    }

    private fun showDropIn() {
        // Show drop in with configuration
        ryftDropIn.show(
            RyftDropInConfiguration(
                clientSecret = clientSecretInput.text.toString(),
                subAccountId = if (subAccountIdInput.text.any()) {
                    subAccountIdInput.text.toString()
                } else {
                    null
                },
                display = RyftDropInDisplayConfiguration(
                    payButtonTitle = "Pay Demo",
                    usage = RyftDropInUsage.valueOf(usageInput.selectedItem.toString())
                ),
                googlePayConfiguration = RyftDropInGooglePayConfiguration(
                    merchantName = DEMO_MERCHANT_NAME,
                    merchantCountryCode = DEMO_MERCHANT_COUNTRY_CODE
                )
            )
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
        errorMessage: String
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error taking payment")
            .setMessage(errorMessage)
            .setPositiveButton("Try again") { _, _ ->
                showDropIn()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    @Parcelize
    private class Arguments(
        val publicApiKey: String
    ) : Parcelable {
        companion object {
            fun bundleFrom(
                publicApiKey: String
            ): Bundle = Bundle().apply {
                putParcelable(
                    ARGUMENTS_BUNDLE_KEY,
                    Arguments(publicApiKey)
                )
            }
        }
    }

    companion object {
        // Sample app constants
        private const val DEMO_MERCHANT_NAME = "Ryft"
        private const val DEMO_MERCHANT_COUNTRY_CODE = "GB"

        private const val ARGUMENTS_BUNDLE_KEY = "Arguments"

        fun newInstance(publicApiKey: String): DemoFragment {
            val fragment = DemoFragment()
            fragment.arguments = Arguments.bundleFrom(publicApiKey)
            return fragment
        }
    }
}
