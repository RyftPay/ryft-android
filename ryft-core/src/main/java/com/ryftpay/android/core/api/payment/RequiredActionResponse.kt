package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class RequiredActionResponse(
    @JsonProperty("type") val type: String,
    @JsonProperty("url") val url: String
)
