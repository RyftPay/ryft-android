package com.ryftpay.android.core.model.payment

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class PaymentSessionErrorTest {

    @Test
    @Parameters(method = "errorsToExpectedRawValues")
    fun `rawValue should return expected string`(
        error: PaymentSessionError,
        expected: String
    ) {
        error.rawValue shouldBeEqualTo expected
    }

    @Test
    fun `from should return null when input is null`() {
        PaymentSessionError.from(null) shouldBeEqualTo null
    }

    @Test
    @Parameters(method = "inputsToExpectedErrors")
    fun `from should return expected error type`(
        input: String,
        expected: PaymentSessionError
    ) {
        PaymentSessionError.from(input) shouldBeEqualTo expected
    }

    private fun errorsToExpectedRawValues(): Array<Any> = arrayOf(
        arrayOf(PaymentSessionError.BadTrackData, "bad_track_data"),
        arrayOf(PaymentSessionError.DeclinedDoNotHonour, "declined_do_not_honour"),
        arrayOf(PaymentSessionError.ExpiredCard, "expired_card"),
        arrayOf(PaymentSessionError.GatewayReject, "gateway_reject"),
        arrayOf(PaymentSessionError.InsufficientFunds, "insufficient_funds"),
        arrayOf(PaymentSessionError.InvalidCardNumber, "invalid_card_number"),
        arrayOf(PaymentSessionError.RestrictedCard, "restricted_card"),
        arrayOf(PaymentSessionError.SecurityViolation, "security_violation"),
        arrayOf(PaymentSessionError.ThreeDSecureAuthenticationFailure, "3ds_authentication_failure"),
        arrayOf(PaymentSessionError.Unknown, "unknown_error")
    )

    private fun inputsToExpectedErrors(): Array<Any> = arrayOf(
        arrayOf("", PaymentSessionError.Unknown),
        arrayOf("some_random_error", PaymentSessionError.Unknown),
        arrayOf("unknown_error", PaymentSessionError.Unknown),
        arrayOf("bad_track_data", PaymentSessionError.BadTrackData),
        arrayOf("declined_do_not_honour", PaymentSessionError.DeclinedDoNotHonour),
        arrayOf("expired_card", PaymentSessionError.ExpiredCard),
        arrayOf("gateway_reject", PaymentSessionError.GatewayReject),
        arrayOf("insufficient_funds", PaymentSessionError.InsufficientFunds),
        arrayOf("invalid_card_number", PaymentSessionError.InvalidCardNumber),
        arrayOf("restricted_card", PaymentSessionError.RestrictedCard),
        arrayOf("security_violation", PaymentSessionError.SecurityViolation),
        arrayOf("3ds_authentication_failure", PaymentSessionError.ThreeDSecureAuthenticationFailure)
    )
}
