package com.ryftpay.android.ui.extension

import android.net.Uri
import android.webkit.WebResourceRequest

private const val PAYMENT_SESSION_QUERY_PARAMETER = "ps"

internal fun WebResourceRequest.isThreeDSecureCompleted(expectedUrl: Uri): Boolean =
    url.host == expectedUrl.host && url.queryParameterNames.contains(PAYMENT_SESSION_QUERY_PARAMETER)

internal fun WebResourceRequest.extractPaymentSessionId(): String? =
    url.getQueryParameter(PAYMENT_SESSION_QUERY_PARAMETER)
