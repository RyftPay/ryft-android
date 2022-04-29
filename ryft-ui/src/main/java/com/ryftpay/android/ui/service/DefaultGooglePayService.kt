package com.ryftpay.android.ui.service

import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import com.ryftpay.android.ui.model.RyftCardType
import org.json.JSONArray
import org.json.JSONObject

internal class DefaultGooglePayService(
    private val paymentsClient: PaymentsClient
) : GooglePayService {

    override fun isReadyToPay(): Task<Boolean> {
        return paymentsClient.isReadyToPay(isReadyToPayRequest)
    }

    private val baseRequest: JSONObject = JSONObject()
        .put("apiVersion", MAJOR_API_VERSION)
        .put("apiVersionMinor", MINOR_API_VERSION)

    private val cardPaymentMethod: JSONObject = JSONObject()
        .put("type", CARD_PAYMENT_METHOD)
        .put(
            "parameters",
            JSONObject()
                .put("allowedAuthMethods", JSONArray(SUPPORTED_AUTH_METHODS))
                .put("allowedCardNetworks", JSONArray(RyftCardType.getGooglePaySupportedTypeNames()))
        )

    private val allowedPaymentMethods: JSONArray = JSONArray()
        .put(cardPaymentMethod)

    private val isReadyToPayRequest: IsReadyToPayRequest = IsReadyToPayRequest.fromJson(
        baseRequest.apply {
            put("existingPaymentMethodRequired", IS_EXISTING_PAYMENT_METHOD_REQUIRED)
                .put("allowedPaymentMethods", allowedPaymentMethods)
        }.toString()
    )

    companion object {
        private const val MAJOR_API_VERSION = 2
        private const val MINOR_API_VERSION = 0
        private const val CARD_PAYMENT_METHOD = "CARD"
        private const val IS_EXISTING_PAYMENT_METHOD_REQUIRED = true
        private val SUPPORTED_AUTH_METHODS = arrayOf("PAN_ONLY", "CRYPTOGRAM_3DS")
    }
}
