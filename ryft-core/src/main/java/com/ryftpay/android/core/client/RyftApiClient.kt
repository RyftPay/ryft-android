package com.ryftpay.android.core.client

import com.ryftpay.android.core.api.payment.AttemptPaymentRequest
import com.ryftpay.android.core.api.payment.PaymentSessionResponse
import com.ryftpay.android.core.model.api.HttpHeaders
import com.ryftpay.android.core.model.api.RyftApiParameters
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RyftApiClient {

    @POST("payment-sessions/attempt-payment")
    fun attemptPayment(
        @Header(HttpHeaders.ACCOUNT) subAccountId: String?,
        @Body body: AttemptPaymentRequest
    ): Call<PaymentSessionResponse>

    @GET("payment-sessions/{${RyftApiParameters.PAYMENT_SESSION_ID}}")
    fun loadPaymentSession(
        @Header(HttpHeaders.ACCOUNT) subAccountId: String?,
        @Path(RyftApiParameters.PAYMENT_SESSION_ID) paymentSessionId: String,
        @Query(RyftApiParameters.CLIENT_SECRET) clientSecret: String
    ): Call<PaymentSessionResponse>
}
