package com.ryftpay.android.core.api.error

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class RyftErrorResponse(
    @JsonProperty("requestId") val requestId: String?,
    @JsonProperty("code") val code: String,
    @JsonProperty("errors") val errors: List<RyftErrorElementResponse>
)
