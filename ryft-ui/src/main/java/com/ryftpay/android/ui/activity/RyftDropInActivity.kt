package com.ryftpay.android.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.wallet.PaymentData
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.ui.dropin.RyftDropInConfiguration
import com.ryftpay.android.ui.dropin.RyftPaymentResult
import com.ryftpay.android.ui.fragment.RyftPaymentFragment
import com.ryftpay.android.ui.model.GooglePayPaymentData
import com.ryftpay.android.ui.model.GooglePayResult
import com.ryftpay.android.ui.service.GooglePayService
import com.ryftpay.android.ui.util.RyftPublicApiKeyParceler
import com.ryftpay.android.ui.viewmodel.GooglePayResultViewModel
import com.ryftpay.android.ui.viewmodel.RyftPaymentResultViewModel
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.lang.IllegalArgumentException

internal class RyftDropInActivity : AppCompatActivity() {

    private lateinit var paymentResultViewModel: RyftPaymentResultViewModel
    private lateinit var googlePayResultViewModel: GooglePayResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ryft_dropin)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupPaymentResultViewModel()
        setupGooglePayResultViewModel()
        val input = intent.getParcelableExtra<Arguments>(ARGUMENTS_INTENT_EXTRA)
            ?: throw IllegalArgumentException("No arguments provided to activity")
        if (savedInstanceState == null) {
            val navHostFragment = NavHostFragment.create(
                R.navigation.nav_graph_ryft_dropin,
                RyftPaymentFragment.Arguments.bundleFrom(
                    input.configuration,
                    input.publicApiKey
                )
            )
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_ryft_nav_host, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Google advises the use of AutoResolveHelper.resolveTask() (see DefaultGooglePayService.kt)
        // which forces the use of this deprecated method
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GooglePayService.LOAD_PAYMENT_DATA_REQUEST_CODE ->
                handleGooglePayResult(resultCode, data)
        }
    }

    private fun handleGooglePayResult(resultCode: Int, maybeIntent: Intent?) {
        val googlePayResult = when (resultCode) {
            RESULT_OK -> maybeIntent?.let { intent ->
                PaymentData.getFromIntent(intent)?.let { paymentData ->
                    GooglePayResult.Ok(GooglePayPaymentData.from(paymentData))
                }
            } ?: GooglePayResult.Failed
            RESULT_CANCELED -> GooglePayResult.Cancelled
            else -> GooglePayResult.Failed
        }
        googlePayResultViewModel.updateResult(googlePayResult)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    private fun setupPaymentResultViewModel() {
        paymentResultViewModel = ViewModelProvider(this)[RyftPaymentResultViewModel::class.java]
        paymentResultViewModel.getResult().observe(this) { paymentResult ->
            returnPaymentResult(paymentResult)
        }
    }

    private fun setupGooglePayResultViewModel() {
        googlePayResultViewModel = ViewModelProvider(this)[GooglePayResultViewModel::class.java]
    }

    private fun returnPaymentResult(paymentResult: RyftPaymentResult) {
        setResult(
            RESULT_OK,
            Intent().apply {
                putExtra(
                    PAYMENT_RESULT_INTENT_EXTRA,
                    paymentResult
                )
            }
        )
        finish()
    }

    @Parcelize
    private class Arguments(
        val configuration: RyftDropInConfiguration,
        val publicApiKey: @WriteWith<RyftPublicApiKeyParceler> RyftPublicApiKey
    ) : Parcelable

    companion object {
        internal const val PAYMENT_RESULT_INTENT_EXTRA = "com.ryftpay.android.ui.dropin.RyftPaymentResult"
        private const val ARGUMENTS_INTENT_EXTRA = "com.ryftpay.android.ui.activity.RyftDropInActivity.Arguments"

        internal fun createIntent(
            context: Context,
            configuration: RyftDropInConfiguration,
            publicApiKey: RyftPublicApiKey
        ): Intent = Intent(context, RyftDropInActivity::class.java).apply {
            putExtra(
                ARGUMENTS_INTENT_EXTRA,
                Arguments(
                    configuration,
                    publicApiKey
                )
            )
        }
    }
}
