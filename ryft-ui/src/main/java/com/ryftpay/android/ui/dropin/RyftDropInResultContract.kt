package com.ryftpay.android.ui.dropin

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.ryftpay.android.ui.activity.RyftDropInActivity

internal class RyftDropInResultContract : ActivityResultContract<Intent, RyftPaymentResult>() {
    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): RyftPaymentResult =
        when (resultCode) {
            Activity.RESULT_OK -> {
                intent?.extras?.getParcelable(RyftDropInActivity.PAYMENT_RESULT_INTENT_EXTRA)
                    ?: RyftPaymentResult.Failed(RyftPaymentError.Unexpected)
            }
            Activity.RESULT_CANCELED -> RyftPaymentResult.Cancelled
            else -> RyftPaymentResult.Failed(RyftPaymentError.Unexpected)
        }
}
