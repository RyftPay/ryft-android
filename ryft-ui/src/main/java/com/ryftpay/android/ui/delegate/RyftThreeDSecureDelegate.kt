package com.ryftpay.android.ui.delegate

import android.net.Uri
import android.view.View

internal interface RyftThreeDSecureDelegate {
    fun onViewCreated(root: View, redirectUri: Uri, returnUri: Uri)
    fun resetView(redirectUri: Uri, returnUri: Uri)
}
