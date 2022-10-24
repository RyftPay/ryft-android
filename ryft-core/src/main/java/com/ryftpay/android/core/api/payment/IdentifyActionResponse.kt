package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class IdentifyActionResponse(
    @JsonProperty("sessionId") val sessionId: String,
    @JsonProperty("sessionSecret") val sessionSecret: String,
    @JsonProperty("scheme") val scheme: String,
    @JsonProperty("paymentMethodId") val paymentMethodId: String
)
