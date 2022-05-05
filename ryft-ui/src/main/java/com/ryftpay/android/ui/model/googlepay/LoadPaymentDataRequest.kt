package com.ryftpay.android.ui.model.googlepay

import org.json.JSONArray
import org.json.JSONObject

internal data class LoadPaymentDataRequest(
    val merchantInfo: MerchantInfo,
    val tokenizationSpecification: TokenizationSpecification,
    val transactionInfo: TransactionInfo
) {
    internal fun toApiV2RequestJson(
        baseApiRequest: BaseApiRequest
    ): JSONObject = JSONObject(
        baseApiRequest.toApiRequestJson().toString()
    ).apply {
        put(MerchantInfo.KEY, merchantInfo.toApiV2RequestJson())
        put(
            PaymentMethod.ALLOWED_PAYMENT_METHODS_KEY,
            JSONArray()
                .put(CardPaymentMethod().toApiV2RequestJson(tokenizationSpecification))
        )
        put(TransactionInfo.KEY, transactionInfo.toApiV2RequestJson())
    }
}
