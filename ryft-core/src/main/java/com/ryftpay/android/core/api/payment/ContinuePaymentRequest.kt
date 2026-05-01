package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.ThreeDsTransactionParams

data class ContinuePaymentRequest(
    @JsonProperty("clientSecret") val clientSecret: String,
    @JsonProperty("threeDs") val threeDs: ThreeDsDetails
) {
    data class ThreeDsDetails(
        @JsonProperty("appAuthentication") val appAuthentication: AppAuthentication
    )

    data class AppAuthentication(
        @JsonProperty("sdkAppId") val sdkAppId: String,
        @JsonProperty("sdkEncData") val sdkEncData: String,
        @JsonProperty("sdkEphemeralPublicKey") val sdkEphemeralPublicKey: String,
        @JsonProperty("sdkMaxTimeoutInMinutes") val sdkMaxTimeoutInMinutes: Int,
        @JsonProperty("sdkReferenceNumber") val sdkReferenceNumber: String,
        @JsonProperty("sdkTransId") val sdkTransId: String,
        @JsonProperty("deviceRenderOptions") val deviceRenderOptions: DeviceRenderOptions
    )

    data class DeviceRenderOptions(
        @JsonProperty("sdkInterface") val sdkInterface: String,
        @JsonProperty("sdkUiTypes") val sdkUiTypes: List<String>
    )

    companion object {
        private const val SDK_MAX_TIMEOUT_MINUTES = 10
        private const val SDK_INTERFACE = "01"
        private val SDK_UI_TYPES = listOf("01", "02", "03")

        internal fun from(
            clientSecret: String,
            params: ThreeDsTransactionParams
        ): ContinuePaymentRequest = ContinuePaymentRequest(
            clientSecret = clientSecret,
            threeDs = ThreeDsDetails(
                appAuthentication = AppAuthentication(
                    sdkAppId = params.sdkApplicationId,
                    sdkEncData = params.sdkEncryptedData,
                    sdkEphemeralPublicKey = params.sdkEphemeralPublicKey,
                    sdkMaxTimeoutInMinutes = SDK_MAX_TIMEOUT_MINUTES,
                    sdkReferenceNumber = params.sdkReferenceNumber,
                    sdkTransId = params.sdkTransactionId,
                    deviceRenderOptions = DeviceRenderOptions(
                        sdkInterface = SDK_INTERFACE,
                        sdkUiTypes = SDK_UI_TYPES
                    )
                )
            )
        )
    }
}
