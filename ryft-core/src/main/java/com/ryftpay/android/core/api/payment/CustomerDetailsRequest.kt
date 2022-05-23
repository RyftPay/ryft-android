package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.CustomerDetails

data class CustomerDetailsRequest(
    @JsonProperty("email") val email: String
) {
    companion object {
        internal fun from(
            customerDetails: CustomerDetails?
        ): CustomerDetailsRequest? = if (customerDetails == null) {
            null
        } else CustomerDetailsRequest(
            email = customerDetails.email
        )
    }
}
