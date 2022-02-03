package com.ryftpay.android.core.model.error

import com.ryftpay.android.core.TestData.ryftErrorElementResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftErrorElementTest {

    @Test
    fun `from should return expected fields`() {
        val ryftErrorElement = RyftErrorElement.from(ryftErrorElementResponse)
        ryftErrorElement.code shouldBeEqualTo ryftErrorElementResponse.code
        ryftErrorElement.message shouldBeEqualTo ryftErrorElementResponse.message
    }
}
