package com.ryftpay.android.core.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ryftpay.android.core.TestData.CLIENT_SECRET
import com.ryftpay.android.core.TestData.LAST_PAYMENT_ERROR
import com.ryftpay.android.core.TestData.PAYMENT_SESSION_ID
import com.ryftpay.android.core.TestData.SUB_ACCOUNT_ID
import com.ryftpay.android.core.TestData.cardDetails
import com.ryftpay.android.core.TestData.customerDetails
import com.ryftpay.android.core.TestData.paymentSessionResponse
import com.ryftpay.android.core.TestData.requiredActionResponse
import com.ryftpay.android.core.TestData.ryftErrorResponse
import com.ryftpay.android.core.api.payment.AttemptPaymentRequest
import com.ryftpay.android.core.api.payment.PaymentSessionResponse
import com.ryftpay.android.core.client.RyftApiClient
import com.ryftpay.android.core.model.error.RyftError
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionError
import com.ryftpay.android.core.model.payment.PaymentSessionStatus
import com.ryftpay.android.core.service.listener.RyftLoadPaymentListener
import com.ryftpay.android.core.service.listener.RyftPaymentResultListener
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import okhttp3.ResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(JUnitParamsRunner::class)
internal class DefaultRyftPaymentServiceTest {

    private val client = mockk<RyftApiClient>(relaxed = true)
    private val paymentResultListener = mockk<RyftPaymentResultListener>(relaxed = true)
    private val loadPaymentListener = mockk<RyftLoadPaymentListener>(relaxed = true)
    private val paymentService = DefaultRyftPaymentService(client)
    private val paymentMethod = PaymentMethod.card(cardDetails)

    @Test
    fun `attemptPayment calls listener on attempting payment`() {
        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onAttemptingPayment()
        }
    }

    @Test
    fun `attemptPayment calls client without sub account id and customer details when they are null`() {
        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        val expected = AttemptPaymentRequest.from(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null
        )

        verify {
            client.attemptPayment(
                subAccountId = null,
                body = match {
                    it == expected
                }
            )
        }
    }

    @Test
    fun `attemptPayment calls client with sub account id when sub account id is present`() {
        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            SUB_ACCOUNT_ID,
            listener = paymentResultListener
        )

        val expected = AttemptPaymentRequest.from(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null
        )

        verify {
            client.attemptPayment(
                SUB_ACCOUNT_ID,
                body = match {
                    it == expected
                }
            )
        }
    }

    @Test
    fun `attemptPayment calls client with customer details when customer details is present`() {
        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails,
            subAccountId = null,
            listener = paymentResultListener
        )

        val expected = AttemptPaymentRequest.from(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails
        )

        verify {
            client.attemptPayment(
                subAccountId = null,
                body = match {
                    it == expected
                }
            )
        }
    }

    @Test
    fun `attemptPayment calls listener on error when api throws an exception`() {
        val exception = Exception("bang")
        givenAttemptPaymentThrows(exception)

        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(error = null, exception)
        }
    }

    @Test
    fun `attemptPayment calls listener on error with unknown error when api returns unexpected error response`() {
        val response = Response.error<PaymentSessionResponse>(500, ResponseBody.create(null, "injiojf-3"))
        givenAttemptPaymentReturns(response)

        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(RyftError.Unknown, throwable = null)
        }
    }

    @Test
    fun `attemptPayment calls listener on error with expected error when api returns ryft error response`() {
        val response = Response.error<PaymentSessionResponse>(
            500,
            ResponseBody.create(null, ObjectMapper().writeValueAsString(ryftErrorResponse))
        )
        givenAttemptPaymentReturns(response)

        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(RyftError.from(ryftErrorResponse), throwable = null)
        }
    }

    @Test
    @Parameters(method = "unexpectedPaymentSessions")
    fun `attemptPayment calls listener on error with unknown error when api returns successful response with unexpected payment session`(
        unexpectedPayment: PaymentSessionResponse
    ) {
        val response = Response.success(unexpectedPayment)
        givenAttemptPaymentReturns(response)

        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(RyftError.Unknown, throwable = null)
        }
    }

    @Test
    fun `attemptPayment calls listener on user error with expected error when api returns response with payment session containing last error`() {
        val paymentWithUserError = paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingPayment.toString(),
            lastError = LAST_PAYMENT_ERROR,
            requiredAction = null
        )
        val response = Response.success(paymentWithUserError)
        givenAttemptPaymentReturns(response)

        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onPaymentHasError(
                PaymentSessionError.from(LAST_PAYMENT_ERROR)!!
            )
        }
        verify(exactly = 0) {
            paymentResultListener.onErrorObtainingPaymentResult(any(), any())
        }
    }

    @Test
    fun `attemptPayment calls listener on payment requires redirect when api returns successful response with payment session requiring redirect`() {
        val paymentRequiringRedirect = paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingAction.toString(),
            requiredAction = requiredActionResponse
        )
        val response = Response.success(paymentRequiringRedirect)
        givenAttemptPaymentReturns(response)

        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onPaymentRequiresRedirect(
                paymentRequiringRedirect.returnUrl,
                requiredActionResponse.url
            )
        }
        verify(exactly = 0) {
            paymentResultListener.onErrorObtainingPaymentResult(any(), any())
        }
    }

    @Test
    @Parameters(method = "approvedPaymentSessions")
    fun `attemptPayment calls listener on payment approved when api returns successful response with approved payment session`(
        approvedPayment: PaymentSessionResponse
    ) {
        val response = Response.success(approvedPayment)
        givenAttemptPaymentReturns(response)

        paymentService.attemptPayment(
            CLIENT_SECRET,
            paymentMethod,
            customerDetails = null,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onPaymentApproved(PaymentSession.from(approvedPayment))
        }
        verify(exactly = 0) {
            paymentResultListener.onErrorObtainingPaymentResult(any(), any())
        }
    }

    @Test
    fun `getLatestPaymentResult calls listener on loading payment session`() {
        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onLoadingPaymentResult()
        }
    }

    @Test
    fun `getLatestPaymentResult calls client without sub account id when sub account id is null`() {
        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            client.loadPaymentSession(subAccountId = null, PAYMENT_SESSION_ID, CLIENT_SECRET)
        }
    }

    @Test
    fun `getLatestPaymentResult calls client with sub account id when sub account id is present`() {
        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            SUB_ACCOUNT_ID,
            listener = paymentResultListener
        )

        verify {
            client.loadPaymentSession(SUB_ACCOUNT_ID, PAYMENT_SESSION_ID, CLIENT_SECRET)
        }
    }

    @Test
    fun `getLatestPaymentResult calls listener on error when api throws an exception`() {
        val exception = Exception("bang")
        givenLoadPaymentSessionThrows(exception)

        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(error = null, exception)
        }
    }

    @Test
    fun `getLatestPaymentResult calls listener on error with unknown error when api returns unexpected error response`() {
        val response = Response.error<PaymentSessionResponse>(500, ResponseBody.create(null, "injiojf-3"))
        givenLoadPaymentSessionReturns(response)

        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(RyftError.Unknown, throwable = null)
        }
    }

    @Test
    fun `getLatestPaymentResult calls listener on error with expected error when api returns ryft error response`() {
        val response = Response.error<PaymentSessionResponse>(
            500,
            ResponseBody.create(null, ObjectMapper().writeValueAsString(ryftErrorResponse))
        )
        givenLoadPaymentSessionReturns(response)

        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(RyftError.from(ryftErrorResponse), throwable = null)
        }
    }

    @Test
    @Parameters(method = "unexpectedPaymentSessions")
    fun `getLatestPaymentResult calls listener on error with unknown error when api returns successful response with unexpected payment session`(
        unexpectedPayment: PaymentSessionResponse
    ) {
        val response = Response.success(unexpectedPayment)
        givenLoadPaymentSessionReturns(response)

        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onErrorObtainingPaymentResult(RyftError.Unknown, throwable = null)
        }
    }

    @Test
    fun `getLatestPaymentResult calls listener on user error with expected error when api returns response with payment session containing last error`() {
        val paymentWithUserError = paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingPayment.toString(),
            lastError = LAST_PAYMENT_ERROR,
            requiredAction = null
        )
        val response = Response.success(paymentWithUserError)
        givenLoadPaymentSessionReturns(response)

        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onPaymentHasError(
                PaymentSessionError.from(LAST_PAYMENT_ERROR)!!
            )
        }
        verify(exactly = 0) {
            paymentResultListener.onErrorObtainingPaymentResult(any(), any())
        }
    }

    @Test
    fun `getLatestPaymentResult calls listener on payment requires redirect when api returns successful response with payment session requiring redirect`() {
        val paymentRequiringRedirect = paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingAction.toString(),
            requiredAction = requiredActionResponse
        )
        val response = Response.success(paymentRequiringRedirect)
        givenLoadPaymentSessionReturns(response)

        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onPaymentRequiresRedirect(
                paymentRequiringRedirect.returnUrl,
                requiredActionResponse.url
            )
        }
        verify(exactly = 0) {
            paymentResultListener.onErrorObtainingPaymentResult(any(), any())
        }
    }

    @Test
    @Parameters(method = "approvedPaymentSessions")
    fun `getLatestPaymentResult calls listener on payment approved when api returns successful response with approved payment session`(
        approvedPayment: PaymentSessionResponse
    ) {
        val response = Response.success(approvedPayment)
        givenLoadPaymentSessionReturns(response)

        paymentService.getLatestPaymentResult(
            PAYMENT_SESSION_ID,
            CLIENT_SECRET,
            subAccountId = null,
            listener = paymentResultListener
        )

        verify {
            paymentResultListener.onPaymentApproved(PaymentSession.from(approvedPayment))
        }
        verify(exactly = 0) {
            paymentResultListener.onErrorObtainingPaymentResult(any(), any())
        }
    }

    @Test
    fun `loadPaymentSession calls listener on loading payment session`() {
        paymentService.loadPaymentSession(
            CLIENT_SECRET,
            subAccountId = null,
            listener = loadPaymentListener
        )

        verify {
            loadPaymentListener.onLoadingPayment()
        }
    }

    @Test
    fun `loadPaymentSession calls client without sub account id when sub account id is null`() {
        paymentService.loadPaymentSession(
            clientSecret = "ps_123_secret_456",
            subAccountId = null,
            listener = loadPaymentListener
        )

        verify {
            client.loadPaymentSession(
                subAccountId = null,
                paymentSessionId = "ps_123",
                clientSecret = "ps_123_secret_456"
            )
        }
    }

    @Test
    fun `loadPaymentSession calls client with sub account id when sub account id is present`() {
        paymentService.loadPaymentSession(
            clientSecret = "ps_123_secret_456",
            SUB_ACCOUNT_ID,
            listener = loadPaymentListener
        )

        verify {
            client.loadPaymentSession(
                SUB_ACCOUNT_ID,
                paymentSessionId = "ps_123",
                clientSecret = "ps_123_secret_456"
            )
        }
    }

    @Test
    fun `loadPaymentSession calls listener on error when api throws an exception`() {
        val exception = Exception("bang")
        givenLoadPaymentSessionThrows(exception)

        paymentService.loadPaymentSession(
            CLIENT_SECRET,
            subAccountId = null,
            listener = loadPaymentListener
        )

        verify {
            loadPaymentListener.onErrorLoadingPayment(error = null, exception)
        }
    }

    @Test
    fun `loadPaymentSession calls listener on error with unknown error when api returns unexpected error response`() {
        val response = Response.error<PaymentSessionResponse>(500, ResponseBody.create(null, "injiojf-3"))
        givenLoadPaymentSessionReturns(response)

        paymentService.loadPaymentSession(
            CLIENT_SECRET,
            subAccountId = null,
            listener = loadPaymentListener
        )

        verify {
            loadPaymentListener.onErrorLoadingPayment(RyftError.Unknown, throwable = null)
        }
    }

    @Test
    fun `loadPaymentSession calls listener on error with expected error when api returns ryft error response`() {
        val response = Response.error<PaymentSessionResponse>(
            500,
            ResponseBody.create(null, ObjectMapper().writeValueAsString(ryftErrorResponse))
        )
        givenLoadPaymentSessionReturns(response)

        paymentService.loadPaymentSession(
            CLIENT_SECRET,
            subAccountId = null,
            listener = loadPaymentListener
        )

        verify {
            loadPaymentListener.onErrorLoadingPayment(RyftError.from(ryftErrorResponse), throwable = null)
        }
    }

    @Test
    fun `loadPaymentSession calls listener on payment loaded when api returns response with payment session`() {
        val payment = paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingPayment.toString(),
            lastError = null,
            requiredAction = null
        )
        givenLoadPaymentSessionReturns(
            Response.success(payment)
        )

        paymentService.loadPaymentSession(
            CLIENT_SECRET,
            subAccountId = null,
            listener = loadPaymentListener
        )

        verify {
            loadPaymentListener.onPaymentLoaded(
                PaymentSession.from(payment)
            )
        }
        verify(exactly = 0) {
            loadPaymentListener.onErrorLoadingPayment(any(), any())
        }
    }

    private fun unexpectedPaymentSessions(): Array<PaymentSessionResponse> = arrayOf(
        paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingPayment.toString(),
            requiredAction = null,
            lastError = null
        ),
        paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingPayment.toString(),
            requiredAction = requiredActionResponse,
            lastError = null
        ),
        paymentSessionResponse.copy(
            status = PaymentSessionStatus.PendingAction.toString(),
            requiredAction = null,
            lastError = null
        ),
        paymentSessionResponse.copy(
            status = PaymentSessionStatus.Unknown.toString(),
            requiredAction = null,
            lastError = null
        ),
        paymentSessionResponse.copy(
            status = PaymentSessionStatus.Unknown.toString(),
            requiredAction = requiredActionResponse,
            lastError = null
        )
    )

    private fun approvedPaymentSessions(): Array<PaymentSessionResponse> = arrayOf(
        paymentSessionResponse.copy(
            status = PaymentSessionStatus.Approved.toString(),
            requiredAction = null,
            lastError = null
        ),
        paymentSessionResponse.copy(
            status = PaymentSessionStatus.Captured.toString(),
            requiredAction = null,
            lastError = null
        )
    )

    private fun givenAttemptPaymentThrows(e: Exception) {
        val mockedCall = mockk<Call<PaymentSessionResponse>>(relaxed = true)
        givenAttemptPaymentCallReturns(mockedCall)

        every {
            mockedCall.enqueue(any<Callback<PaymentSessionResponse>>())
        } answers {
            val callback = args.filterIsInstance<Callback<PaymentSessionResponse>>()[0]
            callback.onFailure(mockedCall, e)
        }
    }

    private fun givenAttemptPaymentReturns(response: Response<PaymentSessionResponse>) {
        val mockedCall = mockk<Call<PaymentSessionResponse>>(relaxed = true)
        givenAttemptPaymentCallReturns(mockedCall)

        every {
            mockedCall.enqueue(any<Callback<PaymentSessionResponse>>())
        } answers {
            val callback = args.filterIsInstance<Callback<PaymentSessionResponse>>()[0]
            callback.onResponse(mockedCall, response)
        }
    }

    private fun givenAttemptPaymentCallReturns(call: Call<PaymentSessionResponse>) {
        every {
            client.attemptPayment(subAccountId = any(), body = any())
        } answers {
            call
        }
    }

    private fun givenLoadPaymentSessionThrows(e: Exception) {
        val mockedCall = mockk<Call<PaymentSessionResponse>>(relaxed = true)
        givenLoadPaymentSessionCallReturns(mockedCall)

        every {
            mockedCall.enqueue(any<Callback<PaymentSessionResponse>>())
        } answers {
            val callback = args.filterIsInstance<Callback<PaymentSessionResponse>>()[0]
            callback.onFailure(mockedCall, e)
        }
    }

    private fun givenLoadPaymentSessionReturns(response: Response<PaymentSessionResponse>) {
        val mockedCall = mockk<Call<PaymentSessionResponse>>(relaxed = true)
        givenLoadPaymentSessionCallReturns(mockedCall)

        every {
            mockedCall.enqueue(any<Callback<PaymentSessionResponse>>())
        } answers {
            val callback = args.filterIsInstance<Callback<PaymentSessionResponse>>()[0]
            callback.onResponse(mockedCall, response)
        }
    }

    private fun givenLoadPaymentSessionCallReturns(call: Call<PaymentSessionResponse>) {
        every {
            client.loadPaymentSession(
                subAccountId = any(),
                paymentSessionId = any(),
                clientSecret = any()
            )
        } answers {
            call
        }
    }
}
