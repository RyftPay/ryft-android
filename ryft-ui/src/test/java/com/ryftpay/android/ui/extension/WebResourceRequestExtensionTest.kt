package com.ryftpay.android.ui.extension

import android.net.Uri
import android.webkit.WebResourceRequest
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

internal class WebResourceRequestExtensionTest {

    private val paymentSessionQueryParameter = "ps"
    private val request = mockk<WebResourceRequest>(relaxed = true)
    private val expectedUri = mockk<Uri>(relaxed = true)
    private val expectedHost = "example"
    private val paymentSessionId = "ps_123"

    @Before
    internal fun setup() {
        every {
            expectedUri.host
        } answers {
            expectedHost
        }
    }

    @Test
    internal fun `isThreeDSecureCompleted should return false when host does not match expected`() {
        every {
            request.url.host
        } answers {
            "somethingelse"
        }

        request.isThreeDSecureCompleted(expectedUri) shouldBeEqualTo false
    }

    @Test
    internal fun `isThreeDSecureCompleted should return false when host matches but there are no query parameters`() {
        every {
            request.url.host
        } answers {
            expectedHost
        }

        every {
            request.url.queryParameterNames
        } answers {
            setOf()
        }

        request.isThreeDSecureCompleted(expectedUri) shouldBeEqualTo false
    }

    @Test
    internal fun `isThreeDSecureCompleted should return false when host matches but there is no payment session query parameter`() {
        every {
            request.url.host
        } answers {
            expectedHost
        }

        every {
            request.url.queryParameterNames
        } answers {
            setOf(
                "something",
                "payment"
            )
        }

        request.isThreeDSecureCompleted(expectedUri) shouldBeEqualTo false
    }

    @Test
    internal fun `isThreeDSecureCompleted should return true when host matches and payment session query parameter found`() {
        every {
            request.url.host
        } answers {
            expectedHost
        }

        every {
            request.url.queryParameterNames
        } answers {
            setOf(
                paymentSessionQueryParameter
            )
        }

        request.isThreeDSecureCompleted(expectedUri) shouldBeEqualTo true
    }

    @Test
    internal fun `extractPaymentSessionId should return null when no payment session query parameter found`() {
        every {
            request.url.getQueryParameter(paymentSessionQueryParameter)
        } answers {
            null
        }

        request.extractPaymentSessionId() shouldBeEqualTo null
    }

    @Test
    internal fun `extractPaymentSessionId should return id when payment session query parameter is found`() {
        every {
            request.url.getQueryParameter(paymentSessionQueryParameter)
        } answers {
            paymentSessionId
        }

        request.extractPaymentSessionId() shouldBeEqualTo paymentSessionId
    }
}
