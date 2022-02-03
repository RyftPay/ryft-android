package com.ryftpay.android.ui.model

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RyftCardCvcTest {

    @Test
    internal fun `Incomplete should return an incomplete Cvc`() {
        val incomplete = RyftCardCvc.Incomplete
        incomplete.sanitisedCvc shouldBeEqualTo ""
        incomplete.validationState shouldBeEqualTo ValidationState.Incomplete
    }

    @Test
    @Parameters(method = "expectedSanitisedCvcs")
    internal fun `from should return a sanitised Cvc`(
        input: String?,
        expected: String
    ) {
        RyftCardCvc.from(input, RyftCardType.Unknown).sanitisedCvc shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "incompleteCvcs")
    internal fun `validationState should return incomplete when Cvc is incomplete for that card type`(
        incompleteCvc: String,
        cardType: RyftCardType
    ) {
        RyftCardCvc.from(incompleteCvc, cardType)
            .validationState shouldBeEqualTo ValidationState.Incomplete
    }

    @Test
    @Parameters(method = "invalidCvcs")
    internal fun `validationState should return invalid when Cvc is invalid for that card type`(
        invaliCvc: String,
        cardType: RyftCardType
    ) {
        RyftCardCvc.from(invaliCvc, cardType)
            .validationState shouldBeEqualTo ValidationState.Invalid
    }

    @Test
    @Parameters(method = "validCvcs")
    internal fun `validationState should return valid when Cvc is valid for that card type`(
        validCvc: String,
        cardType: RyftCardType
    ) {
        RyftCardCvc.from(validCvc, cardType)
            .validationState shouldBeEqualTo ValidationState.Valid
    }

    private fun expectedSanitisedCvcs(): Array<Any> = arrayOf(
        arrayOf(null, ""),
        arrayOf("", ""),
        arrayOf("   ", ""),
        arrayOf("asdasfas", ""),
        arrayOf("asda sfas", ""),
        arrayOf("'#][.", ""),
        arrayOf("123#", ""),
        arrayOf("123a", ""),
        arrayOf("0", "0"),
        arrayOf("1", "1"),
        arrayOf("1234", "1234")
    )

    private fun incompleteCvcs(): Array<Any> = arrayOf(
        arrayOf("", RyftCardType.Unknown),
        arrayOf("", RyftCardType.Visa),
        arrayOf("", RyftCardType.Amex),
        arrayOf("", RyftCardType.Mastercard),
        arrayOf("5", RyftCardType.Unknown),
        arrayOf("9", RyftCardType.Visa),
        arrayOf("2", RyftCardType.Amex),
        arrayOf("4", RyftCardType.Mastercard),
        arrayOf("25", RyftCardType.Unknown),
        arrayOf("63", RyftCardType.Visa),
        arrayOf("89", RyftCardType.Amex),
        arrayOf("90", RyftCardType.Mastercard),
        arrayOf("374", RyftCardType.Unknown),
        arrayOf("738", RyftCardType.Amex),
        arrayOf("374", RyftCardType.Unknown),
    )

    private fun invalidCvcs(): Array<Any> = arrayOf(
        arrayOf("8374", RyftCardType.Visa),
        arrayOf("93047", RyftCardType.Amex),
        arrayOf("3827", RyftCardType.Mastercard),
        arrayOf("294489", RyftCardType.Visa),
        arrayOf("493729", RyftCardType.Amex),
        arrayOf("72839", RyftCardType.Mastercard)
    )

    private fun validCvcs(): Array<Any> = arrayOf(
        arrayOf("784", RyftCardType.Visa),
        arrayOf("8167", RyftCardType.Amex),
        arrayOf("634", RyftCardType.Mastercard),
        arrayOf("392", RyftCardType.Visa),
        arrayOf("4782", RyftCardType.Amex),
        arrayOf("190", RyftCardType.Mastercard)
    )
}
