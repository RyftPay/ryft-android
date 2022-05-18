package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.Address

data class BillingAddressRequest(
    @JsonProperty("firstName") val firstName: String?,
    @JsonProperty("lastName") val lastName: String?,
    @JsonProperty("lineOne") val lineOne: String?,
    @JsonProperty("lineTwo") val lineTwo: String?,
    @JsonProperty("city") val city: String?,
    @JsonProperty("country") val country: String,
    @JsonProperty("postalCode") val postalCode: String,
    @JsonProperty("region") val region: String?,
) {
    companion object {
        internal fun from(billingAddress: Address?): BillingAddressRequest? =
            if (billingAddress == null) {
                null
            } else {
                BillingAddressRequest(
                    billingAddress.firstName,
                    billingAddress.lastName,
                    billingAddress.lineOne,
                    billingAddress.lineTwo,
                    billingAddress.city,
                    billingAddress.country,
                    billingAddress.postalCode,
                    billingAddress.region
                )
            }
    }
}
