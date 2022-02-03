package com.ryftpay.android.core.model.api

import com.ryftpay.android.core.TestData.prodPublicApiKey
import com.ryftpay.android.core.TestData.sandboxPublicApiKey
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftApiTest {

    @Test
    fun `generateBaseApiUrl should return Sandbox url for Sandbox public api keys`() {
        RyftApi.generateBaseApiUrl(sandboxPublicApiKey) shouldBeEqualTo "https://sandbox-api.ryftpay.com/v1/"
    }

    @Test
    fun `generateBaseApiUrl should return Prod url for Prod public api keys`() {
        RyftApi.generateBaseApiUrl(prodPublicApiKey) shouldBeEqualTo "https://api.ryftpay.com/v1/"
    }
}
