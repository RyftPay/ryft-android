package com.ryftpay.android.ui.dropin

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.PaymentSessionError
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import java.io.IOException

@Parcelize
class RyftPaymentError(
    val paymentSessionError: PaymentSessionError? = null,
    val displayError: String
) : Parcelable {

    companion object {
        private const val RESOURCE_TYPE_STRING = "string"
        private const val RESOURCE_STRING_PREFIX = "ryft_"
        private const val UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred"

        val Unexpected = RyftPaymentError(
            displayError = UNEXPECTED_ERROR_MESSAGE
        )

        // We need to access string resources based on a dynamic string
        @SuppressLint("DiscouragedApi")
        internal fun from(
            paymentSessionError: PaymentSessionError,
            context: Context
        ): RyftPaymentError = RyftPaymentError(
            paymentSessionError,
            displayError = context.getString(
                context.resources.getIdentifier(
                    RESOURCE_STRING_PREFIX + paymentSessionError.rawValue,
                    RESOURCE_TYPE_STRING,
                    context.packageName
                )
            )
        )

        internal fun from(
            error: RyftError?,
            throwable: Throwable?,
            context: Context
        ): RyftPaymentError {
            val stringResourceId = when (error?.httpStatusCode) {
                "403" -> R.string.ryft_invalid_api_key_developer_error_message
                "404" -> R.string.ryft_payment_not_found_developer_error_message
                else -> if (throwable is IOException)
                    R.string.ryft_network_error
                else
                    R.string.ryft_unknown_error
            }
            return RyftPaymentError(
                displayError = context.getString(stringResourceId)
            )
        }
    }
}
