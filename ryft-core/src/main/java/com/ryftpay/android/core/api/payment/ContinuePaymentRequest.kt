package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.ThreeDsTransactionParams
import java.util.Base64

data class ContinuePaymentRequest(
    @JsonProperty("clientSecret") val clientSecret: String,
    @JsonProperty("threeDs") val threeDs: ThreeDsDetails
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ThreeDsDetails(
        @JsonProperty("appAuthentication") val appAuthentication: AppAuthentication?,
        @JsonProperty("challengeResult") val challengeResult: String?
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

        // 01 == Native
        private const val SDK_INTERFACE = "01"

        // These are challenge types:
        // 01 == Text field
        // 02 == Single select field
        // 03 == Multi select field
        // The Ravelin SDK will dictate which is used, we just provide options
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
                ),
                challengeResult = null
            )
        )

        internal fun fromChallengeResult(
            clientSecret: String,
            transactionStatus: String,
            threeDSServerTransactionId: String
        ): ContinuePaymentRequest {
            val json = """{"transStatus":"$transactionStatus","threeDSServerTransID":"$threeDSServerTransactionId"}"""
            val encoded = Base64.getEncoder().encodeToString(json.toByteArray())
            return ContinuePaymentRequest(
                clientSecret = clientSecret,
                threeDs = ThreeDsDetails(
                    appAuthentication = null,
                    challengeResult = encoded
                )
            )
        }
    }
}
