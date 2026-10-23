package com.ryftpay.android.ui.dropin

import android.content.Context
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.error.RyftErrorElement
import com.ryftpay.android.core.model.payment.PaymentSessionError
import com.ryftpay.ui.R
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.io.IOException

internal class RyftPaymentErrorTest {

    private val context = mockk<Context>()

    init {
        every { context.getString(R.string.ryft_unknown_error) } returns "unknown error"
        every { context.getString(R.string.ryft_network_error) } returns "network error"
        every { context.getString(R.string.ryft_invalid_api_key_developer_error_message) } returns "invalid api key"
        every { context.getString(R.string.ryft_payment_not_found_developer_error_message) } returns "payment not found"
    }

    @Test
    internal fun `Unexpected should return expected error`() {
        val error = RyftPaymentError.Unexpected
        error.paymentSessionError shouldBeEqualTo null
        error.displayError shouldBeEqualTo "An unexpected error occurred"
        error.errorCode shouldBeEqualTo null
    }

    @Test
    internal fun `constructor should default error code to null`() {
        val error = RyftPaymentError(
            paymentSessionError = PaymentSessionError.InsufficientFunds,
            displayError = "display"
        )
        error.paymentSessionError shouldBeEqualTo PaymentSessionError.InsufficientFunds
        error.displayError shouldBeEqualTo "display"
        error.errorCode shouldBeEqualTo null
    }

    @Test
    internal fun `from should return api error message and code when status is 400 with errors`() {
        val error = RyftPaymentError.from(
            ryftError("400", RyftErrorElement("card_declined", "The card was declined")),
            throwable = null,
            context
        )
        error.displayError shouldBeEqualTo "The card was declined"
        error.errorCode shouldBeEqualTo "card_declined"
        error.paymentSessionError shouldBeEqualTo null
    }

    @Test
    internal fun `from should return unknown error when status is 400 without errors`() {
        val error = RyftPaymentError.from(ryftError("400"), throwable = null, context)
        error.displayError shouldBeEqualTo "unknown error"
        error.errorCode shouldBeEqualTo null
    }

    @Test
    internal fun `from should return unknown error when status is 400 with blank message`() {
        val error = RyftPaymentError.from(
            ryftError("400", RyftErrorElement("bad_request", " ")),
            throwable = null,
            context
        )
        error.displayError shouldBeEqualTo "unknown error"
        error.errorCode shouldBeEqualTo "bad_request"
    }

    @Test
    internal fun `from should return invalid api key error and code when status is 403`() {
        val error = RyftPaymentError.from(
            ryftError("403", RyftErrorElement("forbidden", "Forbidden")),
            throwable = null,
            context
        )
        error.displayError shouldBeEqualTo "invalid api key"
        error.errorCode shouldBeEqualTo "forbidden"
    }

    @Test
    internal fun `from should return payment not found error and code when status is 404`() {
        val error = RyftPaymentError.from(
            ryftError("404", RyftErrorElement("not_found", "Not found")),
            throwable = null,
            context
        )
        error.displayError shouldBeEqualTo "payment not found"
        error.errorCode shouldBeEqualTo "not_found"
    }

    @Test
    internal fun `from should return unknown error and code when status is not mapped`() {
        val error = RyftPaymentError.from(
            ryftError("500", RyftErrorElement("internal_error", "Internal error")),
            throwable = null,
            context
        )
        error.displayError shouldBeEqualTo "unknown error"
        error.errorCode shouldBeEqualTo "internal_error"
    }

    @Test
    internal fun `from should return network error when throwable is IOException without ryft error`() {
        val error = RyftPaymentError.from(error = null, IOException("timeout"), context)
        error.displayError shouldBeEqualTo "network error"
        error.errorCode shouldBeEqualTo null
    }

    @Test
    internal fun `from should return unknown error when throwable is not IOException without ryft error`() {
        val error = RyftPaymentError.from(error = null, IllegalStateException("boom"), context)
        error.displayError shouldBeEqualTo "unknown error"
        error.errorCode shouldBeEqualTo null
    }

    @Test
    internal fun `from should return unknown error when neither ryft error nor throwable is provided`() {
        val error = RyftPaymentError.from(error = null, throwable = null, context)
        error.displayError shouldBeEqualTo "unknown error"
        error.errorCode shouldBeEqualTo null
    }

    private fun ryftError(
        httpStatusCode: String,
        vararg errors: RyftErrorElement
    ) = RyftError(
        requestId = "req_123",
        httpStatusCode = httpStatusCode,
        errors = errors.toList()
    )
}
