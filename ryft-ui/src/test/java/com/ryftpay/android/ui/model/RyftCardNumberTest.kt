package com.ryftpay.android.ui.model

import com.ryftpay.android.ui.TestData.invalidAmexRawCardNumbers
import com.ryftpay.android.ui.TestData.invalidMastercardRawCardNumbers
import com.ryftpay.android.ui.TestData.invalidVisaRawCardNumbers
import com.ryftpay.android.ui.TestData.validAmexRawCardNumbers
import com.ryftpay.android.ui.TestData.validMastercardRawCardNumbers
import com.ryftpay.android.ui.TestData.validVisaRawCardNumbers
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RyftCardNumberTest {

    @Test
    internal fun `Incomplete should return an incomplete card number`() {
        val incomplete = RyftCardNumber.Incomplete
        incomplete.sanitisedNumber shouldBeEqualTo ""
        incomplete.validationState shouldBeEqualTo ValidationState.Incomplete
    }

    @Test
    @Parameters(method = "expectedSanitisedCardNumbers")
    internal fun `from should return a sanitised card number`(
        input: String?,
        expected: String
    ) {
        RyftCardNumber.from(input).sanitisedNumber shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "incompleteCardNumbers")
    internal fun `validationState should return incomplete when card number is too short`(
        incompleteCardNumber: String
    ) {
        RyftCardNumber.from(incompleteCardNumber)
            .validationState shouldBeEqualTo ValidationState.Incomplete
    }

    @Test
    @Parameters(method = "invalidCardNumbers")
    internal fun `validationState should return invalid when card number is the correct length but luhn check fails`(
        invalidCardNumber: String
    ) {
        RyftCardNumber.from(invalidCardNumber)
            .validationState shouldBeEqualTo ValidationState.Invalid
    }

    @Test
    @Parameters(method = "validCardNumbers")
    internal fun `validationState should return valid when card number is the correct length and luhn check succeeds`(
        validCardNumber: String
    ) {
        RyftCardNumber.from(validCardNumber)
            .validationState shouldBeEqualTo ValidationState.Valid
    }

    @Test
    @Parameters(method = "expectedFormattedCardNumbersTrailingAllowed")
    internal fun `formatted should always return card number with spaces in expected positions when specified`(
        input: String,
        expected: String
    ) {
        RyftCardNumber.from(input)
            .formatted(removeTrailingSeparator = false) shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "expectedFormattedCardNumbersTrailingNotAllowed")
    internal fun `formatted should only return card number with spaces in expected positions if it's not trailing when specified`(
        input: String,
        expected: String
    ) {
        RyftCardNumber.from(input)
            .formatted(removeTrailingSeparator = true) shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "cardNumbersToExpectedTypes")
    internal fun `derivedType should return expected type`(
        input: String,
        expected: RyftCardType
    ) {
        RyftCardNumber.from(input).derivedType shouldBeEqualTo expected
    }

    private fun incompleteCardNumbers(): Array<String> = arrayOf(
        "",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "12",
        "34",
        "4",
        "37",
        "51",
        "54",
        "2251",
        "2369",
        "4242 4242 4",
        "4242 4242 4242 424",
        "3400 0000",
        "3400 000000 0000",
        "2251 0000 000",
        "2251 0000 0000 000",
        "1234 1234 1234 1234 12"
    )

    private fun invalidCardNumbers(): Array<String> =
        invalidVisaRawCardNumbers +
            invalidMastercardRawCardNumbers +
            invalidAmexRawCardNumbers +
            invalidUnknownCardNumbers()

    private fun invalidUnknownCardNumbers(): Array<String> = arrayOf(
        "1234 1234 1234 1234 123",
        "6759 0000 0000 0000 005"
    )

    private fun validCardNumbers(): Array<String> =
        validVisaRawCardNumbers +
            validMastercardRawCardNumbers +
            validAmexRawCardNumbers

    private fun expectedSanitisedCardNumbers(): Array<Any> = arrayOf(
        arrayOf(null, ""),
        arrayOf("", ""),
        arrayOf("   ", ""),
        arrayOf("joaisjdi", ""),
        arrayOf("nsd oindc", ""),
        arrayOf(", oindc", ""),
        arrayOf("1234a", ""),
        arrayOf("1234,", ""),
        arrayOf("0", "0"),
        arrayOf("1", "1"),
        arrayOf("1234", "1234"),
        arrayOf("0123 4", "01234"),
        arrayOf("1234 5678", "12345678"),
        arrayOf("1234 5678 9012 3456", "1234567890123456"),
        arrayOf("1234567890123456", "1234567890123456")
    )

    private fun expectedFormattedCardNumbersTrailingAllowed(): Array<Any> = arrayOf(
        arrayOf("", ""),
        arrayOf("1", "1"),
        arrayOf("1234", "1234"),
        arrayOf("12345678", "12345678"),
        arrayOf("1234567890123456", "1234567890123456"),
        arrayOf("1234567890123456789", "1234567890123456789"),
        arrayOf("1234567890123456789012", "1234567890123456789"),
        arrayOf("4", "4"),
        arrayOf("4234", "4234 "),
        arrayOf("423456", "4234 56"),
        arrayOf("42345678", "4234 5678 "),
        arrayOf("42345678901", "4234 5678 901"),
        arrayOf("4234567890123", "4234 5678 9012 3"),
        arrayOf("4234567890123456", "4234 5678 9012 3456"),
        arrayOf("42345678901234567890", "4234 5678 9012 3456"),
        arrayOf("34", "34"),
        arrayOf("3423", "3423 "),
        arrayOf("342345", "3423 45"),
        arrayOf("34234567", "3423 4567"),
        arrayOf("3423456789", "3423 456789 "),
        arrayOf("34234567890", "3423 456789 0"),
        arrayOf("3423456789012", "3423 456789 012"),
        arrayOf("342345678901234", "3423 456789 01234"),
        arrayOf("3423456789012347890", "3423 456789 01234")
    )

    private fun expectedFormattedCardNumbersTrailingNotAllowed(): Array<Any> = arrayOf(
        arrayOf("", ""),
        arrayOf("1", "1"),
        arrayOf("1234", "1234"),
        arrayOf("12345678", "12345678"),
        arrayOf("1234567890123456", "1234567890123456"),
        arrayOf("1234567890123456789", "1234567890123456789"),
        arrayOf("1234567890123456789012", "1234567890123456789"),
        arrayOf("4", "4"),
        arrayOf("4234", "4234"),
        arrayOf("423456", "4234 56"),
        arrayOf("42345678", "4234 5678"),
        arrayOf("42345678901", "4234 5678 901"),
        arrayOf("4234567890123", "4234 5678 9012 3"),
        arrayOf("42345678901234567890", "4234 5678 9012 3456"),
        arrayOf("34", "34"),
        arrayOf("3423", "3423"),
        arrayOf("342345", "3423 45"),
        arrayOf("34234567", "3423 4567"),
        arrayOf("3423456789", "3423 456789"),
        arrayOf("34234567890", "3423 456789 0"),
        arrayOf("3423456789012", "3423 456789 012"),
        arrayOf("3423456789012347890", "3423 456789 01234")
    )

    private fun cardNumbersToExpectedTypes(): Array<Any> = arrayOf(
        arrayOf("", RyftCardType.Unknown),
        arrayOf(validVisaRawCardNumbers[0], RyftCardType.Visa),
        arrayOf(validMastercardRawCardNumbers[0], RyftCardType.Mastercard),
        arrayOf(validAmexRawCardNumbers[0], RyftCardType.Amex)
    )
}
