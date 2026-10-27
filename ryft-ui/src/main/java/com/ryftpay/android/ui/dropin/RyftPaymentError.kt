package com.ryftpay.android.ui.dropin

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
    val displayError: String,
    val errorCode: String? = null,
    val errorMessage: String? = null
) : Parcelable {

    @Deprecated("Maintained for binary compatibility", level = DeprecationLevel.HIDDEN)
    constructor(
        paymentSessionError: PaymentSessionError? = null,
        displayError: String
    ) : this(
        paymentSessionError = paymentSessionError,
        displayError = displayError,
        errorCode = null,
        errorMessage = null
    )

    companion object {
        private const val UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred"

        val Unexpected = RyftPaymentError(
            displayError = UNEXPECTED_ERROR_MESSAGE
        )

        internal fun from(
            paymentSessionError: PaymentSessionError,
            context: Context
        ): RyftPaymentError {
            val stringResourceId = when (paymentSessionError) {
                PaymentSessionError.BadTrackData -> R.string.ryft_bad_track_data
                PaymentSessionError.Cvv2Failure -> R.string.ryft_cvv2_failure
                PaymentSessionError.DeclinedDoNotHonour -> R.string.ryft_declined_do_not_honour
                PaymentSessionError.ExpiredCard -> R.string.ryft_expired_card
                PaymentSessionError.GatewayReject -> R.string.ryft_gateway_reject
                PaymentSessionError.InsufficientFunds -> R.string.ryft_insufficient_funds
                PaymentSessionError.InvalidCardNumber -> R.string.ryft_invalid_card_number
                PaymentSessionError.RestrictedCard -> R.string.ryft_restricted_card
                PaymentSessionError.SecurityViolation -> R.string.ryft_security_violation
                PaymentSessionError.ThreeDSecureAuthenticationFailure -> R.string.ryft_3ds_authentication_failure
                PaymentSessionError.Unknown -> R.string.ryft_unknown_error
            }
            return RyftPaymentError(
                paymentSessionError = paymentSessionError,
                displayError = context.getString(stringResourceId)
            )
        }

        internal fun from(
            error: RyftError?,
            throwable: Throwable?,
            context: Context
        ): RyftPaymentError {
            val apiError = error?.errors?.firstOrNull()
            val stringResourceId = when (error?.httpStatusCode) {
                "403" -> R.string.ryft_invalid_api_key_developer_error_message
                "404" -> R.string.ryft_payment_not_found_developer_error_message
                else -> if (throwable is IOException) {
                    R.string.ryft_network_error
                } else {
                    R.string.ryft_unknown_error
                }
            }
            return RyftPaymentError(
                displayError = context.getString(stringResourceId),
                errorCode = apiError?.code?.takeIf { it.isNotBlank() },
                errorMessage = apiError?.message?.takeIf { it.isNotBlank() }
            )
        }
    }
}
