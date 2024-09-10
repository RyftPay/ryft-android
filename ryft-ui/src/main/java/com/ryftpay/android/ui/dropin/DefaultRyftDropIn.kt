package com.ryftpay.android.ui.dropin

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.ui.activity.RyftDropInActivity

class DefaultRyftDropIn : RyftDropIn {

    private val launcher: ActivityResultLauncher<Intent>
    private val context: Context
    private var publicApiKey: RyftPublicApiKey? = null

    constructor(
        fragment: Fragment,
        listener: RyftDropInResultListener,
        publicApiKey: RyftPublicApiKey
    ) {
        launcher = fragment.registerForActivityResult(
            RyftDropInResultContract(),
            listener::onPaymentResult
        )
        context = fragment.requireContext()
        this.publicApiKey = publicApiKey
    }

    constructor(
        activity: ComponentActivity,
        listener: RyftDropInResultListener,
        publicApiKey: RyftPublicApiKey
    ) {
        this.launcher = activity.registerForActivityResult(
            RyftDropInResultContract(),
            listener::onPaymentResult
        )
        context = activity
        this.publicApiKey = publicApiKey
    }

    constructor(
        fragment: Fragment,
        listener: RyftDropInResultListener,
    ) {
        launcher = fragment.registerForActivityResult(
            RyftDropInResultContract(),
            listener::onPaymentResult
        )
        context = fragment.requireContext()
    }

    constructor(
        activity: ComponentActivity,
        listener: RyftDropInResultListener,
    ) {
        this.launcher = activity.registerForActivityResult(
            RyftDropInResultContract(),
            listener::onPaymentResult
        )
        context = activity
    }

    override fun show(
        configuration: RyftDropInConfiguration
    ) {
        val publicApiKey = configuration.publicApiKey ?: this.publicApiKey ?: throw IllegalArgumentException("publicApiKey was null")
        launcher.launch(
            RyftDropInActivity.createIntent(
                context,
                configuration,
                publicApiKey
            )
        )
    }
}
