package com.ryftpay.android.core.model.api

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class HttpHeadersTest {

    @Test
    fun `Account header should have expected value`() {
        HttpHeaders.ACCOUNT shouldBeEqualTo "Account"
    }

    @Test
    fun `Authorization header should have expected value`() {
        HttpHeaders.AUTHORIZATION shouldBeEqualTo "Authorization"
    }

    @Test
    fun `ContentType header should have expected value`() {
        HttpHeaders.CONTENT_TYPE shouldBeEqualTo "Content-Type"
    }

    @Test
    fun `UserAgent header should have expected value`() {
        HttpHeaders.USER_AGENT shouldBeEqualTo "User-Agent"
    }
}
