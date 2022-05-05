package com.ryftpay.android.ui.model.googlepay

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class BaseApiRequestTest {

    @Test
    fun `toApiRequestJson returns json with api versions`() {
        val requestJson = BaseApiRequest(
            majorApiVersion = 2,
            minorApiVersion = 0
        ).toApiRequestJson()
        requestJson.get("apiVersion") shouldBeEqualTo 2
        requestJson.get("apiVersionMinor") shouldBeEqualTo 0
    }
}
