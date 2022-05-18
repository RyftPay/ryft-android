package com.ryftpay.android.ui.extension

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class StringExtensionTest {

    private val separator = '/'

    @Test
    internal fun `toDigitsOrNull should return null if string is empty`() {
        "".toDigitsOrNull() shouldBeEqualTo null
    }

    @Test
    @Parameters(method = "stringsContainingNonDigits")
    internal fun `toDigitsOrNull should return null if string contains non digit characters`(
        input: String
    ) {
        input.toDigitsOrNull() shouldBeEqualTo null
    }

    @Test
    @Parameters(method = "stringsContainingDigitsOnly")
    internal fun `toDigitsOrNull should return digits if string only contains them`(
        input: String
    ) {
        input.toDigitsOrNull() shouldBeEqualTo input
    }

    @Test
    @Parameters(method = "stringsContainingNonDigitsWithSeparators")
    internal fun `toDigitsOrNull should return null if string contains non digit characters with separators`(
        input: String
    ) {
        input.toDigitsOrNull(separator) shouldBeEqualTo null
    }

    @Test
    @Parameters(method = "stringsContainingDigitsOnlyWithSeparators")
    internal fun `toDigitsOrNull should return digits without separators if string only contains them and separators`(
        input: String
    ) {
        input.toDigitsOrNull(separator) shouldBeEqualTo input.filter { it != separator }
    }

    @Test
    @Parameters(method = "expectedSeparatorsInPositions")
    internal fun `addSeparatorIntoPositions should add separators into desired positions`(
        input: String,
        positions: IntArray,
        expected: String
    ) {
        input.addSeparatorIntoPositions(separator, positions) shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "fullNamesToExpectedPairs")
    internal fun `extractFirstAndLastNamesOrNulls should return first and last name or a null pair`(
        input: String?,
        expected: Pair<String?, String?>
    ) {
        input.extractFirstAndLastNamesOrNulls() shouldBeEqualTo expected
    }

    private fun stringsContainingNonDigits(): Array<Any> = arrayOf(
        arrayOf("iok0934"),
        arrayOf("abc"),
        arrayOf("123a"),
        arrayOf("123."),
        arrayOf("123 "),
        arrayOf(" 123"),
        arrayOf("123'"),
        arrayOf("123£"),
        arrayOf("1 2"),
        arrayOf("_12"),
        arrayOf("/12"),
        arrayOf("/#][")
    )

    private fun stringsContainingDigitsOnly(): Array<String> = arrayOf(
        "0",
        "1",
        "9",
        "23",
        "0934",
        "123"
    )

    private fun stringsContainingNonDigitsWithSeparators(): Array<Any> = arrayOf(
        arrayOf("iok${separator}0934"),
        arrayOf("ab${separator}c"),
        arrayOf("12${separator}3a"),
        arrayOf("1${separator}23."),
        arrayOf("123$separator "),
        arrayOf(" 12${separator}3"),
        arrayOf("12${separator}3'"),
        arrayOf("1${separator}23£"),
        arrayOf("1 ${separator}2"),
        arrayOf("_${separator}12"),
        arrayOf("[1${separator}2"),
        arrayOf("&#]$separator[")
    )

    private fun stringsContainingDigitsOnlyWithSeparators(): Array<String> = arrayOf(
        "0$separator",
        "${separator}1",
        "9$separator",
        "2${separator}3",
        "093${separator}4",
        "1${separator}23"
    )

    private fun expectedSeparatorsInPositions(): Array<Any> = arrayOf(
        arrayOf("hello", intArrayOf(1, 3), "h${separator}el${separator}lo"),
        arrayOf("hello", intArrayOf(2), "he${separator}llo"),
        arrayOf("hello", intArrayOf(5), "hello$separator"),
        arrayOf("hello", intArrayOf(), "hello"),
        arrayOf("", intArrayOf(2), ""),
        arrayOf("no", intArrayOf(5), "no")
    )

    private fun fullNamesToExpectedPairs(): Array<Any> = arrayOf(
        arrayOf(null, Pair<String?, String?>(null, null)),
        arrayOf("", Pair<String?, String?>(null, null)),
        arrayOf("John", Pair<String?, String?>("John", null)),
        arrayOf("John ", Pair<String?, String?>("John", null)),
        arrayOf("John Doe", Pair<String?, String?>("John", "Doe")),
        arrayOf("John Bob Doe", Pair<String?, String?>("John", "Doe")),
        arrayOf("John Bob-Doe", Pair<String?, String?>("John", "Bob-Doe")),
        arrayOf("John Bob Bob-Doe", Pair<String?, String?>("John", "Bob-Doe")),
        arrayOf("Miss Jenny Doe", Pair<String?, String?>("Jenny", "Doe")),
        arrayOf("Mr John Doe", Pair<String?, String?>("John", "Doe")),
        arrayOf("Mrs Jenny Doe", Pair<String?, String?>("Jenny", "Doe")),
        arrayOf("Ms Jenny Doe", Pair<String?, String?>("Jenny", "Doe")),
        arrayOf("Miss Jenny Sarah Doe", Pair<String?, String?>("Jenny", "Doe")),
        arrayOf("Mr John Bob Doe", Pair<String?, String?>("John", "Doe")),
        arrayOf("Mrs Jenny Sarah Doe", Pair<String?, String?>("Jenny", "Doe")),
        arrayOf("Ms Jenny Sarah Doe", Pair<String?, String?>("Jenny", "Doe")),
        arrayOf("Miss Jenny Sarah-Doe", Pair<String?, String?>("Jenny", "Sarah-Doe")),
        arrayOf("Mr John Bob-Doe", Pair<String?, String?>("John", "Bob-Doe")),
        arrayOf("Mrs Jenny Sarah-Doe", Pair<String?, String?>("Jenny", "Sarah-Doe")),
        arrayOf("Ms Jenny Sarah-Doe", Pair<String?, String?>("Jenny", "Sarah-Doe")),
        arrayOf("Miss J Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("Mr J Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("Mrs J Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("Ms J Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("Miss J S Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("Mr J B Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("Mrs J S Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("Ms J S Doe", Pair<String?, String?>("J", "Doe")),
        arrayOf("JOHN", Pair<String?, String?>("JOHN", null)),
        arrayOf("JOHN ", Pair<String?, String?>("JOHN", null)),
        arrayOf("JOHN DOE", Pair<String?, String?>("JOHN", "DOE")),
        arrayOf("JOHN BOB DOE", Pair<String?, String?>("JOHN", "DOE")),
        arrayOf("JOHN BOB-DOE", Pair<String?, String?>("JOHN", "BOB-DOE")),
        arrayOf("JOHN BOB BOB-DOE", Pair<String?, String?>("JOHN", "BOB-DOE")),
        arrayOf("MISS JENNY SARAH DOE", Pair<String?, String?>("JENNY", "DOE")),
        arrayOf("MR JOHN BOB DOE", Pair<String?, String?>("JOHN", "DOE")),
        arrayOf("MRS JENNY SARAH DOE", Pair<String?, String?>("JENNY", "DOE")),
        arrayOf("MS JENNY SARAH DOE", Pair<String?, String?>("JENNY", "DOE")),
        arrayOf("MISS JENNY SARAH-DOE", Pair<String?, String?>("JENNY", "SARAH-DOE")),
        arrayOf("MR JOHN BOB-DOE", Pair<String?, String?>("JOHN", "BOB-DOE")),
        arrayOf("MRS JENNY SARAH-DOE", Pair<String?, String?>("JENNY", "SARAH-DOE")),
        arrayOf("MS JENNY SARAH-DOE", Pair<String?, String?>("JENNY", "SARAH-DOE")),
        arrayOf("MISS J DOE", Pair<String?, String?>("J", "DOE")),
        arrayOf("MR J DOE", Pair<String?, String?>("J", "DOE")),
        arrayOf("MRS J DOE", Pair<String?, String?>("J", "DOE")),
        arrayOf("MS J DOE", Pair<String?, String?>("J", "DOE")),
        arrayOf("MISS J S DOE", Pair<String?, String?>("J", "DOE")),
        arrayOf("MR J B DOE", Pair<String?, String?>("J", "DOE")),
        arrayOf("MRS J S DOE", Pair<String?, String?>("J", "DOE")),
        arrayOf("MS J S DOE", Pair<String?, String?>("J", "DOE"))
    )
}
