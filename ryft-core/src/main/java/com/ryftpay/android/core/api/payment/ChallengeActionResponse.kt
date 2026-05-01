package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChallengeActionResponse(
    @JsonProperty("threeDSServerTransactionID") val threeDSServerTransactionId: String,
    @JsonProperty("acsTransactionID") val acsTransactionId: String,
    @JsonProperty("acsRefNumber") val acsRefNumber: String,
    @JsonProperty("acsSignedContent") val acsSignedContent: String
)
