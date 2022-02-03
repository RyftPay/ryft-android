package com.ryftpay.android.core.client.interceptor

import com.ryftpay.android.core.model.Ryft
import com.ryftpay.android.core.model.api.HttpHeaders
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class RyftApiRequestInterceptor(
    private val publicApiKey: RyftPublicApiKey
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader(HttpHeaders.AUTHORIZATION, publicApiKey.value)
            .addHeader(HttpHeaders.USER_AGENT, Ryft.USER_AGENT)
            .addHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        private const val APPLICATION_JSON = "application/json"
    }
}
