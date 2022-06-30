package com.ryftpay.android.core.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftTest {

    @Test
    fun `Version should have initial version of sdk`() {
        Ryft.VERSION shouldBeEqualTo "1.1.0"
    }

    @Test
    fun `User agent should identify sdk with initial version`() {
        Ryft.USER_AGENT shouldBeEqualTo "ryft-sdk-android/1.1.0"
    }
}
