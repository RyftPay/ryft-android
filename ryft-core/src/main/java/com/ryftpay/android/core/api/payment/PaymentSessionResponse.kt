package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentSessionResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("amount") val amount: Int,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("returnUrl") val returnUrl: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("customerEmail") val customerEmail: String?,
    @JsonProperty("lastError") val lastError: String?,
    @JsonProperty("requiredAction") val requiredAction: RequiredActionResponse?,
    @JsonProperty("createdTimestamp") val createdTimestamp: Long,
    @JsonProperty("lastUpdatedTimestamp") val lastUpdatedTimestamp: Long
)
