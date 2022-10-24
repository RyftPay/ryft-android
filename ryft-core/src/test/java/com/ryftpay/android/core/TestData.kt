package com.ryftpay.android.core

import com.ryftpay.android.core.api.error.RyftErrorElementResponse
import com.ryftpay.android.core.api.error.RyftErrorResponse
import com.ryftpay.android.core.api.payment.IdentifyActionResponse
import com.ryftpay.android.core.api.payment.PaymentSessionResponse
import com.ryftpay.android.core.api.payment.RequiredActionResponse
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.payment.Address
import com.ryftpay.android.core.model.payment.CardDetails
import com.ryftpay.android.core.model.payment.CustomerDetails
import com.ryftpay.android.core.model.payment.PaymentMethodOptions
import com.ryftpay.android.core.model.payment.PaymentSessionStatus
import com.ryftpay.android.core.model.payment.RequiredActionType
import java.util.UUID

internal object TestData {

    internal const val CLIENT_SECRET = "ps_123_secret_456"
    internal const val PAYMENT_SESSION_ID = "ps_123"
    internal const val PROD_PUBLIC_API_KEY_VALUE = "pk_123"
    internal const val SANDBOX_PUBLIC_API_KEY_VALUE = "pk_sandbox_123"
    internal const val SUB_ACCOUNT_ID = "ac_123"
    internal const val LAST_PAYMENT_ERROR = "invalid_card_number"
    internal const val GOOGLE_PAY_TOKEN = "google_pay_token_123"
    internal const val CUSTOMER_EMAIL = "test@example.com"
    internal const val PAYMENT_METHOD_ID = "pmt_123"

    internal val prodPublicApiKey = RyftPublicApiKey(PROD_PUBLIC_API_KEY_VALUE)
    internal val sandboxPublicApiKey = RyftPublicApiKey(SANDBOX_PUBLIC_API_KEY_VALUE)

    internal val cardDetails = CardDetails(
        number = "4242424242424242",
        expiryMonth = "10",
        expiryYear = "2030",
        cvc = "100"
    )

    internal val paymentMethodOptions = PaymentMethodOptions(
        store = false
    )

    internal val address = Address(
        firstName = "John",
        lastName = "Doe",
        lineOne = "c/o Google LLC",
        lineTwo = "1600 Amphitheatre Pkwy",
        city = "Mountain View",
        country = "US",
        postalCode = "94043",
        region = "CA"
    )

    internal val customerDetails = CustomerDetails(
        email = CUSTOMER_EMAIL
    )

    internal val ryftErrorElementResponse = RyftErrorElementResponse(
        code = "unexpected_error",
        message = "An unexpected error occurred"
    )

    internal val ryftErrorResponse = RyftErrorResponse(
        requestId = UUID.randomUUID().toString(),
        code = "500",
        errors = listOf(ryftErrorElementResponse)
    )

    internal val redirectRequiredActionResponse = RequiredActionResponse(
        type = RequiredActionType.Redirect.toString(),
        url = "https://redirect.to.me/",
        identify = null
    )

    internal val identifyActionResponse = IdentifyActionResponse(
        sessionId = "session_123",
        sessionSecret = "secret_123",
        scheme = "visa",
        paymentMethodId = PAYMENT_METHOD_ID
    )

    internal val identifyRequiredActionResponse = RequiredActionResponse(
        type = RequiredActionType.Identify.toString(),
        url = null,
        identify = identifyActionResponse
    )

    internal val paymentSessionResponse = PaymentSessionResponse(
        id = PAYMENT_SESSION_ID,
        amount = 483,
        currency = "GBP",
        returnUrl = "https://my-url.com",
        status = PaymentSessionStatus.PendingPayment.toString(),
        customerEmail = CUSTOMER_EMAIL,
        lastError = "invalid_card_number",
        requiredAction = redirectRequiredActionResponse,
        createdTimestamp = 1642098636,
        lastUpdatedTimestamp = 1642138419
    )
}
