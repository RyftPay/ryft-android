package com.ryftpay.android.ui.model

import junitparams.JUnitParamsRunner
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RyftCardOptionsTest {

    @Test
    internal fun `Default should set saveForFuture to false`() {
        val default = RyftCardOptions.Default
        default.saveForFuture shouldBeEqualTo false
    }

    @Test
    internal fun `withSaveForFutureUse should copy card options with new flag, preserving other fields`() {
        val currentOptions = RyftCardOptions(
            saveForFuture = false
        )
        val newOptions = currentOptions.withSaveForFuture(saveForFuture = true)
        val expectedOptions = RyftCardOptions(
            saveForFuture = true
        )
        newOptions shouldBeEqualTo expectedOptions
    }
}
