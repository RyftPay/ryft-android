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
    private val apiKey: RyftPublicApiKey

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
        apiKey = publicApiKey
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
        apiKey = publicApiKey
    }

    override fun handle(
        configuration: RyftRequiredActionComponent.Configuration,
        requiredAction: RequiredAction
    ) {
        launcher.launch(
            RyftRequiredActionActivity.createIntent(
                context,
                configuration,
                apiKey,
                requiredAction
            )
        )
    }
}
