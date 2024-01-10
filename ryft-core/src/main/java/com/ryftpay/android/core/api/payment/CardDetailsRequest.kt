package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.CardDetails

data class CardDetailsRequest(
    @JsonProperty("number") val number: String,
    @JsonProperty("expiryMonth") val expiryMonth: String,
    @JsonProperty("expiryYear") val expiryYear: String,
    @JsonProperty("cvc") val cvc: String,
    @JsonProperty("name") val name: String?
) {
    companion object {
        internal fun from(cardDetails: CardDetails): CardDetailsRequest =
            CardDetailsRequest(
                cardDetails.number,
                cardDetails.expiryMonth,
                cardDetails.expiryYear,
                cardDetails.cvc,
                cardDetails.name
            )
    }
}
