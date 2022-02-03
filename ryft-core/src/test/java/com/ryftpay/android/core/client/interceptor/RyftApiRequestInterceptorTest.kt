package com.ryftpay.android.core.client.interceptor

import com.ryftpay.android.core.TestData.sandboxPublicApiKey
import com.ryftpay.android.core.model.Ryft
import com.ryftpay.android.core.model.api.HttpHeaders
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Before
import org.junit.Test

internal class RyftApiRequestInterceptorTest {

    private val interceptorChain = mockk<Interceptor.Chain>(relaxed = true)
    private val request = mockk<Request>(relaxed = true)

    @Before
    fun setup() {
        every { interceptorChain.request() } answers { request }
    }

    @Test
    fun `intercept should add expected headers`() {
        val interceptor = RyftApiRequestInterceptor(sandboxPublicApiKey)
        interceptor.intercept(interceptorChain)
        val expectedRequest = request.newBuilder()
            .addHeader(HttpHeaders.AUTHORIZATION, sandboxPublicApiKey.value)
            .addHeader(HttpHeaders.USER_AGENT, Ryft.USER_AGENT)
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .build()
        verify {
            interceptorChain.proceed(
                match {
                    it == expectedRequest
                }
            )
        }
    }
}
