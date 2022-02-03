package com.ryftpay.android.core.client

import com.ryftpay.android.core.client.interceptor.RyftApiRequestInterceptor
import com.ryftpay.android.core.model.api.RyftApi
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class RyftApiClientFactory(
    private val publicApiKey: RyftPublicApiKey
) {

    private val ryftApiRequestInterceptor: RyftApiRequestInterceptor by lazy {
        RyftApiRequestInterceptor(publicApiKey)
    }

    fun createRyftApiClient(): RyftApiClient {
        val retrofit = Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                    .addInterceptor(ryftApiRequestInterceptor)
                    .build()
            )
            .baseUrl(RyftApi.generateBaseApiUrl(publicApiKey))
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        return retrofit.create(RyftApiClient::class.java)
    }

    companion object {
        private const val READ_TIMEOUT_IN_SECONDS: Long = 10
    }
}
