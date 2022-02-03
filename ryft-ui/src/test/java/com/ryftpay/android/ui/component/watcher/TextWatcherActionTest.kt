package com.ryftpay.android.ui.component.watcher

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class TextWatcherActionTest {

    @Test
    @Parameters(method = "expectedActions")
    internal fun `from should return expected action`(
        textLengthToBeDeleted: Int,
        textLengthToBeAdded: Int,
        expected: TextWatcherAction
    ) {
        TextWatcherAction.from(textLengthToBeDeleted, textLengthToBeAdded) shouldBeEqualTo expected
    }

    private fun expectedActions(): Array<Any> = arrayOf(
        arrayOf(1, 0, TextWatcherAction.Delete),
        arrayOf(0, 1, TextWatcherAction.Insert),
        arrayOf(2, 0, TextWatcherAction.Delete),
        arrayOf(5, 0, TextWatcherAction.Delete),
        arrayOf(0, 4, TextWatcherAction.Insert),
        arrayOf(3, 4, TextWatcherAction.Insert),
        arrayOf(5, 4, TextWatcherAction.Delete),
        arrayOf(0, 0, TextWatcherAction.Unknown),
        arrayOf(3, 3, TextWatcherAction.Unknown)
    )
}
