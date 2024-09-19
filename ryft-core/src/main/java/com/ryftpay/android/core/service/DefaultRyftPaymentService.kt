package com.ryftpay.android.core.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ryftpay.android.core.api.error.RyftErrorResponse
import com.ryftpay.android.core.api.payment.AttemptPaymentRequest
import com.ryftpay.android.core.api.payment.PaymentSessionResponse
import com.ryftpay.android.core.client.RyftApiClient
import com.ryftpay.android.core.extension.extractPaymentSessionIdFromClientSecret
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.CustomerDetails
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionStatus
import com.ryftpay.android.core.model.payment.RequiredActionType
import com.ryftpay.android.core.service.listener.RyftLoadPaymentListener
import com.ryftpay.android.core.service.listener.RyftPaymentResultListener
import com.ryftpay.android.core.service.listener.RyftRawPaymentResultListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DefaultRyftPaymentService(
    private val client: RyftApiClient,
) : RyftPaymentService {

    override fun attemptPayment(
        clientSecret: String,
        paymentMethod: PaymentMethod,
        customerDetails: CustomerDetails?,
        subAccountId: String?,
        listener: RyftPaymentResultListener
    ) {
        listener.onAttemptingPayment()
        val attemptPaymentRequest = AttemptPaymentRequest.from(
            clientSecret,
            paymentMethod,
            customerDetails
        )
        client.attemptPayment(
            subAccountId,
            attemptPaymentRequest
        ).enqueue(
            callbackForPaymentResult(listener)
        )
    }

    override fun getLatestPaymentResult(
        paymentSessionId: String,
        clientSecret: String,
        subAccountId: String?,
        listener: RyftPaymentResultListener
    ) {
        listener.onLoadingPaymentResult()
        client.loadPaymentSession(
            subAccountId,
            paymentSessionId,
            clientSecret
        ).enqueue(
            callbackForPaymentResult(listener)
        )
    }

    override fun loadPaymentSession(
        clientSecret: String,
        subAccountId: String?,
        listener: RyftLoadPaymentListener
    ) {
        listener.onLoadingPayment()
        client.loadPaymentSession(
            subAccountId,
            clientSecret.extractPaymentSessionIdFromClientSecret(),
            clientSecret
        ).enqueue(
            callbackForLoadingPayment(listener)
        )
    }

    private fun callbackForPaymentResult(
        listener: RyftPaymentResultListener
    ): Callback<PaymentSessionResponse> =
        object : Callback<PaymentSessionResponse> {

            override fun onFailure(call: Call<PaymentSessionResponse>, t: Throwable) =
                listener.onErrorObtainingPaymentResult(null, t)

            override fun onResponse(
                call: Call<PaymentSessionResponse>,
                response: Response<PaymentSessionResponse>
            ) {
                if (!response.isSuccessful) {
                    listener.onErrorObtainingPaymentResult(parseRyftError(response), null)
                    return
                }
                response.body()?.let {
                    val paymentSession = PaymentSession.from(it)
                    if (listener is RyftRawPaymentResultListener) {
                        listener.onRawPaymentResult(paymentSession)
                        return
                    }
                    if (paymentSession.status == PaymentSessionStatus.Approved ||
                        paymentSession.status == PaymentSessionStatus.Captured
                    ) {
                        listener.onPaymentApproved(paymentSession)
                        return
                    }
                    if (paymentSession.status == PaymentSessionStatus.PendingAction &&
                        paymentSession.requiredAction != null &&
                        paymentSession.requiredAction.type == RequiredActionType.Redirect &&
                        paymentSession.requiredAction.url != null
                    ) {
                        listener.onPaymentRequiresRedirect(
                            paymentSession.returnUrl,
                            paymentSession.requiredAction.url
                        )
                        return
                    }
                    if (paymentSession.status == PaymentSessionStatus.PendingAction &&
                        paymentSession.requiredAction != null &&
                        paymentSession.requiredAction.type == RequiredActionType.Identify &&
                        paymentSession.requiredAction.identify != null
                    ) {
                        listener.onPaymentRequiresIdentification(
                            paymentSession.returnUrl,
                            paymentSession.requiredAction.identify
                        )
                        return
                    }
                    if (paymentSession.status == PaymentSessionStatus.PendingPayment &&
                        paymentSession.lastError != null
                    ) {
                        listener.onPaymentHasError(
                            paymentSession.lastError
                        )
                        return
                    }
                }
                listener.onErrorObtainingPaymentResult(RyftError.Unknown, null)
            }
        }

    private fun callbackForLoadingPayment(
        listener: RyftLoadPaymentListener
    ): Callback<PaymentSessionResponse> =
        object : Callback<PaymentSessionResponse> {

            override fun onFailure(call: Call<PaymentSessionResponse>, t: Throwable) =
                listener.onErrorLoadingPayment(null, t)

            override fun onResponse(
                call: Call<PaymentSessionResponse>,
                response: Response<PaymentSessionResponse>
            ) {
                if (!response.isSuccessful) {
                    listener.onErrorLoadingPayment(parseRyftError(response), null)
                    return
                }
                response.body()?.let {
                    listener.onPaymentLoaded(PaymentSession.from(it))
                    return
                }
                listener.onErrorLoadingPayment(RyftError.Unknown, null)
            }
        }

    private fun parseRyftError(
        response: Response<PaymentSessionResponse>
    ): RyftError =
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
