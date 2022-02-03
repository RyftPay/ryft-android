package com.ryftpay.android.ui.model

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(JUnitParamsRunner::class)
internal class RyftCardExpiryDateTest {

    @Test
    internal fun `MaxFormattedLength should return length of date plus separator`() {
        RyftCardExpiryDate.MAX_FORMATTED_LENGTH shouldBeEqualTo 5
    }

    @Test
    internal fun `DateFormat should return MM{forward-slash}yy`() {
        RyftCardExpiryDate.DateFormat.toPattern() shouldBeEqualTo "MM/yy"
    }

    @Test
    internal fun `Incomplete should return an incomplete expiry date`() {
        val incomplete = RyftCardExpiryDate.Incomplete
        incomplete.sanitisedExpiryDate shouldBeEqualTo ""
        incomplete.validationState shouldBeEqualTo ValidationState.Incomplete
    }

    @Test
    @Parameters(method = "expectedSanitisedExpiryDates")
    internal fun `from should return a sanitised expiry date`(
        input: String?,
        expected: String
    ) {
        RyftCardExpiryDate.from(input).sanitisedExpiryDate shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "expectedTwoDigitMonths")
    internal fun `twoDigitMonth should return the a two digit month`(
        input: String?,
        expected: String
    ) {
        RyftCardExpiryDate.from(input).twoDigitMonth shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "expectedTwoDigitYears")
    internal fun `twoDigitYear should return the a two digit year`(
        input: String?,
        expected: String
    ) {
        RyftCardExpiryDate.from(input).twoDigitYear shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "expectedFourDigitYears")
    internal fun `fourDigitYear should return the a four digit year`(
        input: String?,
        expected: String
    ) {
        RyftCardExpiryDate.from(input).fourDigitYear shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "incompleteExpiryDates")
    internal fun `validationState should return incomplete when expiry date could be in the future but is too short`(
        incompleteExpiryDate: String
    ) {
        RyftCardExpiryDate.from(incompleteExpiryDate)
            .validationState shouldBeEqualTo ValidationState.Incomplete
    }

    @Test
    @Parameters(method = "invalidExpiryDates")
    internal fun `validationState should return invalid when expiry date is definitely in the past or not a month`(
        invalidExpiryDate: String
    ) {
        RyftCardExpiryDate.from(invalidExpiryDate)
            .validationState shouldBeEqualTo ValidationState.Invalid
    }

    @Test
    @Parameters(method = "validExpiryDates")
    internal fun `validationState should return valid when expiry date is definitely in the future and is the correct length`(
        validExpiryDate: String
    ) {
        RyftCardExpiryDate.from(validExpiryDate)
            .validationState shouldBeEqualTo ValidationState.Valid
    }

    @Test
    @Parameters(method = "expectedFormattedExpiryDatesTrailingAllowed")
    internal fun `formatted should always return expiry date with a forward slash in the middle when specified`(
        input: String,
        expected: String
    ) {
        RyftCardExpiryDate.from(input)
            .formatted(removeTrailingSeparator = false) shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "expectedFormattedExpiryDatesTrailingNotAllowed")
    internal fun `formatted should only return expiry date with a forward slash if it's in the middle when specified`(
        input: String,
        expected: String
    ) {
        RyftCardExpiryDate.from(input)
            .formatted(removeTrailingSeparator = true) shouldBeEqualTo expected
    }

    private fun incompleteExpiryDates(): Array<String> = arrayOf(
        "",
        "0",
        "1",
        "01",
        "09",
        "10",
        "12",
        "01/2",
        "09/2",
        "01/9",
        "09/9",
        "10/2",
        "12/2",
        "10/9",
        "12/2"
    )

    private fun invalidExpiryDates(): Array<Any> = arrayOf(
        "2",
        "9",
        "13",
        "19",
        "91",
        "01/1",
        "01/0",
        "12/1",
        "12/0",
        "12/02",
        "01/19",
        "11/21",
        getFormattedExpiryDateAfter(months = -1)
    )

    private fun validExpiryDates(): Array<Any> = arrayOf(
        getFormattedExpiryDateAfter(months = 0),
        getFormattedExpiryDateAfter(months = 1),
        getFormattedExpiryDateAfter(months = 12),
        getFormattedExpiryDateAfter(months = 17),
        getFormattedExpiryDateAfter(months = 38),
        getFormattedExpiryDateAfter(months = 93)
    )

    private fun getFormattedExpiryDateAfter(months: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, months)
        return RyftCardExpiryDate.DateFormat.format(calendar.time)
    }

    private fun expectedSanitisedExpiryDates(): Array<Any> = arrayOf(
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
        arrayOf("12", "12"),
        arrayOf("12/", "12"),
        arrayOf("12/3", "123"),
        arrayOf("01/23", "0123"),
        arrayOf("12/25", "1225"),
        arrayOf("07/46", "0746"),
        arrayOf("07/79", "0779"),
        arrayOf("07/89", "0789"),
        arrayOf("09/99", "0999")
    )

    private fun expectedTwoDigitMonths(): Array<Any> = arrayOf(
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
        arrayOf("12", "12"),
        arrayOf("12/", "12"),
        arrayOf("12/3", "12"),
        arrayOf("01/23", "01"),
        arrayOf("12/25", "12"),
        arrayOf("07/30", "07"),
        arrayOf("03/42", "03"),
        arrayOf("10/43", "10"),
        arrayOf("04/79", "04"),
        arrayOf("05/89", "05"),
        arrayOf("09/99", "09")
    )

    private fun expectedTwoDigitYears(): Array<Any> = arrayOf(
        arrayOf(null, ""),
        arrayOf("", ""),
        arrayOf("   ", ""),
        arrayOf("joaisjdi", ""),
        arrayOf("nsd oindc", ""),
        arrayOf(", oindc", ""),
        arrayOf("1234a", ""),
        arrayOf("1234,", ""),
        arrayOf("0", ""),
        arrayOf("1", ""),
        arrayOf("12", ""),
        arrayOf("12/", ""),
        arrayOf("12/3", "3"),
        arrayOf("01/23", "23"),
        arrayOf("12/25", "25"),
        arrayOf("07/30", "30"),
        arrayOf("03/42", "42"),
        arrayOf("10/43", "43"),
        arrayOf("04/79", "79"),
        arrayOf("05/89", "89"),
        arrayOf("09/99", "99")
    )

    private fun expectedFourDigitYears(): Array<Any> = arrayOf(
        arrayOf(null, ""),
        arrayOf("", ""),
        arrayOf("   ", ""),
        arrayOf("joaisjdi", ""),
        arrayOf("nsd oindc", ""),
        arrayOf(", oindc", ""),
        arrayOf("1234a", ""),
        arrayOf("1234,", ""),
        arrayOf("0", ""),
        arrayOf("1", ""),
        arrayOf("12", ""),
        arrayOf("12/", ""),
        arrayOf("12/3", ""),
        arrayOf("01/23", "2023"),
        arrayOf("12/25", "2025"),
        arrayOf("07/30", "2030"),
        arrayOf("03/42", "2042"),
        arrayOf("10/43", "2043"),
        arrayOf("04/79", "2079"),
        arrayOf("05/89", "2089"),
        arrayOf("09/99", "2099")
    )

    private fun expectedFormattedExpiryDatesTrailingAllowed(): Array<Any> = arrayOf(
        arrayOf("", ""),
        arrayOf("0", "0"),
        arrayOf("1", "1"),
        arrayOf("12", "12/"),
        arrayOf("12/", "12/"),
        arrayOf("3", "3"),
        arrayOf("123", "12/3"),
        arrayOf("1224", "12/24"),
        arrayOf("01", "01/"),
        arrayOf("0135", "01/35")
    )

    private fun expectedFormattedExpiryDatesTrailingNotAllowed(): Array<Any> = arrayOf(
        arrayOf("", ""),
        arrayOf("0", "0"),
        arrayOf("1", "1"),
        arrayOf("12", "12"),
        arrayOf("12/", "12"),
        arrayOf("3", "3"),
        arrayOf("123", "12/3"),
        arrayOf("1224", "12/24"),
        arrayOf("01", "01"),
        arrayOf("0135", "01/35")
    )
}
