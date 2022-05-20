package com.ryftpay.android.ui.model.googlepay

import org.json.JSONObject

internal data class BaseApiRequest(
    val majorApiVersion: Int,
    val minorApiVersion: Int
) {

    internal fun toApiRequestJson(): JSONObject = JSONObject()
        .put("apiVersion", majorApiVersion)
        .put("apiVersionMinor", minorApiVersion)
}
