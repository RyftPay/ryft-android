package com.ryftpay.android.ui.delegate

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ryftpay.android.ui.extension.extractPaymentSessionId
import com.ryftpay.android.ui.extension.isThreeDSecureCompleted
import com.ryftpay.android.ui.listener.RyftThreeDSecureListener
import com.ryftpay.ui.R

internal class DefaultRyftThreeDSecureDelegate(
    private val listener: RyftThreeDSecureListener
) : RyftThreeDSecureDelegate {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(
        root: View,
        redirectUri: Uri,
        returnUri: Uri
    ) {
        webView = root.findViewById(R.id.webview_ryft_three_d_secure)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = ThreeDSecureWebViewClient(
            returnUri,
            listener
        )
        webView.loadUrl(redirectUri.toString())
    }

    override fun resetView(
        redirectUri: Uri,
        returnUri: Uri
    ) {
        webView.webViewClient = ThreeDSecureWebViewClient(
            returnUri,
            listener
        )
        webView.loadUrl(redirectUri.toString())
    }

    private class ThreeDSecureWebViewClient(
        private val returnUrl: Uri,
        private val listener: RyftThreeDSecureListener
    ) : WebViewClient() {

        private var paymentSessionId: String? = null

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return if (request?.isThreeDSecureCompleted(returnUrl) == true) {
                paymentSessionId = request.extractPaymentSessionId()
                true
            } else super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            paymentSessionId?.let {
                listener.onThreeDSecureCompleted(it)
            }
        }
    }
}
