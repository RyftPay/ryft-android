package com.ryftpay.android.ui.dropin.threeds

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.ryftpay.android.ui.activity.RyftRequiredActionActivity
import com.ryftpay.android.ui.dropin.RyftPaymentError
import com.ryftpay.android.ui.extension.getParcelableArgs

internal class RyftRequiredActionResultContract : ActivityResultContract<Intent, RyftRequiredActionResult>() {
    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): RyftRequiredActionResult =
        when (resultCode) {
            Activity.RESULT_OK -> {
                intent?.extras?.getParcelableArgs(
                    RyftRequiredActionActivity.REQUIRED_ACTION_RESULT_INTENT_EXTRA,
                    RyftRequiredActionResult::class.java
                ) ?: RyftRequiredActionResult.Error(RyftPaymentError.Unexpected)
            }
            else -> RyftRequiredActionResult.Error(RyftPaymentError.Unexpected)
        }
}
