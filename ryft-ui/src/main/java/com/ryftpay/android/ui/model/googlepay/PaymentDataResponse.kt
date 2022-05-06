package com.ryftpay.android.ui.model.googlepay

import com.google.android.gms.wallet.PaymentData
import org.json.JSONObject

internal data class PaymentDataResponse(
    val token: String
) {
    companion object {
        internal fun from(paymentData: PaymentData): PaymentDataResponse {
            val json = JSONObject(paymentData.toJson())
            val paymentMethodJson = json.getJSONObject("paymentMethodData")
            val token = paymentMethodJson
                .getJSONObject("tokenizationData")
                .get("token")
                .toString()
            return PaymentDataResponse(token)
        }
    }
}
