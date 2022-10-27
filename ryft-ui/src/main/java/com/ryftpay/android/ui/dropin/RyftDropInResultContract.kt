package com.ryftpay.android.ui.dropin

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.ryftpay.android.ui.activity.RyftDropInActivity
import com.ryftpay.android.ui.extension.getParcelableArgs

internal class RyftDropInResultContract : ActivityResultContract<Intent, RyftPaymentResult>() {
    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): RyftPaymentResult =
        when (resultCode) {
            Activity.RESULT_OK -> {
                intent?.extras?.getParcelableArgs(
                    RyftDropInActivity.PAYMENT_RESULT_INTENT_EXTRA,
                    RyftPaymentResult::class.java
                ) ?: RyftPaymentResult.Failed(RyftPaymentError.Unexpected)
            }
            Activity.RESULT_CANCELED -> RyftPaymentResult.Cancelled
            else -> RyftPaymentResult.Failed(RyftPaymentError.Unexpected)
        }
}
