package com.ryftpay.android.ui.dropin

import android.content.Context
import android.os.Parcelable
import com.ryftpay.android.core.model.payment.PaymentSessionError
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import java.io.IOException

@Parcelize
class RyftPaymentError(
    val paymentSessionError: PaymentSessionError?,
    val displayError: String
) : Parcelable {

    companion object {
        private const val RESOURCE_TYPE_STRING = "string"
        private const val RESOURCE_STRING_PREFIX = "ryft_"
        private const val UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred"

        val Unexpected = RyftPaymentError(
            paymentSessionError = null,
            displayError = UNEXPECTED_ERROR_MESSAGE
        )

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
            throwable: Throwable?,
            context: Context
        ): RyftPaymentError {
            val displayError = if (throwable is IOException) {
                context.getString(R.string.ryft_network_error)
            } else {
                context.getString(R.string.ryft_unknown_error)
            }
            return RyftPaymentError(
                paymentSessionError = null,
                displayError
            )
        }
    }
}
