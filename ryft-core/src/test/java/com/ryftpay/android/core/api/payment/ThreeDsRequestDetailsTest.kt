package com.ryftpay.android.core.api.payment

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class ThreeDsRequestDetailsTest {

    @Test
    fun `Application should return request with Application device channel`() {
        val request = ThreeDsRequestDetails.Application
        request.deviceChannel shouldBeEqualTo "Application"
    }
}
