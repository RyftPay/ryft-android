package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty

data class ThreeDsRequestDetails(
    @JsonProperty("deviceChannel") val deviceChannel: String
) {
    companion object {
        internal val Application = ThreeDsRequestDetails(deviceChannel = "Application")
    }
}
