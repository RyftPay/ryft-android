package com.ryftpay.android.core.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftTest {

    @Test
    fun `Version should have current version of sdk`() {
        Ryft.VERSION shouldBeEqualTo "1.4.0"
    }

    @Test
    fun `User agent should identify sdk with current version`() {
        Ryft.USER_AGENT shouldBeEqualTo "ryft-sdk-android/1.4.0"
    }
}
