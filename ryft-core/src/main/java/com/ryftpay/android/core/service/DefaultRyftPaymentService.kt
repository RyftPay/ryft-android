package com.ryftpay.android.core.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ryftpay.android.core.api.error.RyftErrorResponse
import com.ryftpay.android.core.api.payment.AttemptPaymentRequest
import com.ryftpay.android.core.api.payment.PaymentSessionResponse
import com.ryftpay.android.core.client.RyftApiClient
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionStatus
import com.ryftpay.android.core.model.payment.RequiredActionType
import com.ryftpay.android.core.service.listener.RyftPaymentListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DefaultRyftPaymentService(
    private val client: RyftApiClient,
    private val paymentListener: RyftPaymentListener
) : RyftPaymentService {

    override fun attemptPayment(
        clientSecret: String,
        paymentMethod: PaymentMethod,
        subAccountId: String?
    ) {
        paymentListener.onAttemptingPayment()
        val attemptPaymentRequest = AttemptPaymentRequest.from(clientSecret, paymentMethod)
        client.attemptPayment(subAccountId, attemptPaymentRequest).enqueue(paymentSessionResponseCallback())
    }

    override fun loadPaymentSession(
        paymentSessionId: String,
        clientSecret: String,
        subAccountId: String?
    ) {
        paymentListener.onLoadingPayment()
        client.loadPaymentSession(
            subAccountId,
            paymentSessionId,
            clientSecret
        ).enqueue(
            paymentSessionResponseCallback()
        )
    }

    private fun paymentSessionResponseCallback(): Callback<PaymentSessionResponse> =
        object : Callback<PaymentSessionResponse> {

            override fun onFailure(call: Call<PaymentSessionResponse>, t: Throwable) =
                paymentListener.onError(null, t)

            override fun onResponse(
                call: Call<PaymentSessionResponse>,
                response: Response<PaymentSessionResponse>
            ) {
                if (!response.isSuccessful) {
                    paymentListener.onError(parseRyftError(response), null)
                    return
                }
                response.body()?.let {
                    val paymentSession = PaymentSession.from(it)
                    if (paymentSession.status == PaymentSessionStatus.Approved ||
                        paymentSession.status == PaymentSessionStatus.Captured
                    ) {
                        paymentListener.onPaymentApproved(paymentSession)
                        return
                    }
                    if (paymentSession.status == PaymentSessionStatus.PendingAction &&
                        paymentSession.requiredAction != null &&
                        paymentSession.requiredAction.type == RequiredActionType.Redirect
                    ) {
                        paymentListener.onPaymentRequiresRedirect(
                            paymentSession.returnUrl,
                            paymentSession.requiredAction.url
                        )
                        return
                    }
                    if (paymentSession.status == PaymentSessionStatus.PendingPayment &&
                        paymentSession.lastError != null
                    ) {
                        paymentListener.onPaymentHasError(
                            paymentSession.lastError
                        )
                        return
                    }
                }
                paymentListener.onError(RyftError.Unknown, null)
            }

            private fun parseRyftError(response: Response<PaymentSessionResponse>): RyftError =
                try {
                    response.errorBody()?.let {
                        val errorResponse =
                            ObjectMapper().readValue(it.string(), RyftErrorResponse::class.java)
                        RyftError.from(errorResponse)
                    } ?: RyftError.Unknown
                } catch (exception: Exception) {
                    RyftError.Unknown
                }
        }
}
