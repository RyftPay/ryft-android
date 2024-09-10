package com.ryftpay.android.ui.dropin.threeds

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.payment.RequiredAction
import com.ryftpay.android.ui.activity.RyftRequiredActionActivity

class DefaultRyftRequiredActionComponent : RyftRequiredActionComponent {

    private val launcher: ActivityResultLauncher<Intent>
    private val context: Context
    private var publicApiKey: RyftPublicApiKey? = null

    constructor(
        fragment: Fragment,
        listener: RyftRequiredActionResultListener,
        publicApiKey: RyftPublicApiKey
    ) {
        launcher = fragment.registerForActivityResult(
            RyftRequiredActionResultContract(),
            listener::onRequiredActionResult
        )
        context = fragment.requireContext()
        this.publicApiKey = publicApiKey
    }

    constructor(
        activity: ComponentActivity,
        listener: RyftRequiredActionResultListener,
        publicApiKey: RyftPublicApiKey
    ) {
        this.launcher = activity.registerForActivityResult(
            RyftRequiredActionResultContract(),
            listener::onRequiredActionResult
        )
        context = activity
        this.publicApiKey = publicApiKey
    }

    constructor(
        fragment: Fragment,
        listener: RyftRequiredActionResultListener
    ) {
        launcher = fragment.registerForActivityResult(
            RyftRequiredActionResultContract(),
            listener::onRequiredActionResult
        )
        context = fragment.requireContext()
    }

    constructor(
        activity: ComponentActivity,
        listener: RyftRequiredActionResultListener
    ) {
        this.launcher = activity.registerForActivityResult(
            RyftRequiredActionResultContract(),
            listener::onRequiredActionResult
        )
        context = activity
    }

    override fun handle(
        configuration: RyftRequiredActionComponent.Configuration,
        requiredAction: RequiredAction
    ) {
        val publicApiKey = configuration.publicApiKey ?: this.publicApiKey ?: throw IllegalArgumentException("publicApiKey was null")
        launcher.launch(
            RyftRequiredActionActivity.createIntent(
                context,
                configuration,
                publicApiKey,
                requiredAction
            )
        )
    }
}
