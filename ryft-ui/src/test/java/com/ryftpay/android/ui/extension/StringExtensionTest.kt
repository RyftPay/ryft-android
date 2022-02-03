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
}
