package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class IdentifyActionResponse(
    @JsonProperty("scheme") val scheme: String,
    @JsonProperty("messageVersion") val messageVersion: String
)
