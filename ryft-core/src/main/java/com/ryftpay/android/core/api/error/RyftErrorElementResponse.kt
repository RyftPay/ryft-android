package com.ryftpay.android.core.api.error

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class RyftErrorElementResponse(
    @JsonProperty("code") val code: String,
    @JsonProperty("message") val message: String
)
