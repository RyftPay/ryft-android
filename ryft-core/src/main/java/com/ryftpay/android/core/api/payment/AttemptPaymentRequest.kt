package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.CustomerDetails
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentMethodType
import java.lang.IllegalArgumentException

data class AttemptPaymentRequest(
    @JsonProperty("clientSecret") val clientSecret: String,
    @JsonProperty("cardDetails") val cardDetails: CardDetailsRequest?,
    @JsonProperty("walletDetails") val walletDetails: WalletDetailsRequest?,
    @JsonProperty("billingAddress") val billingAddress: BillingAddressRequest?,
    @JsonProperty("customerDetails") val customerDetails: CustomerDetailsRequest?
) {
    companion object {
        internal fun from(
            clientSecret: String,
            paymentMethod: PaymentMethod,
            customerDetails: CustomerDetails?
        ): AttemptPaymentRequest =
            when (paymentMethod.type) {
                PaymentMethodType.Card -> AttemptPaymentRequest(
                    clientSecret,
                    CardDetailsRequest.from(
                        paymentMethod.cardDetails
                            ?: throw IllegalArgumentException("Invalid payment method - card details is null")
                    ),
                    walletDetails = null,
                    BillingAddressRequest.from(paymentMethod.billingAddress),
                    CustomerDetailsRequest.from(customerDetails)
                )
                PaymentMethodType.GooglePay -> AttemptPaymentRequest(
                    clientSecret,
                    cardDetails = null,
                    WalletDetailsRequest.from(
                        paymentMethod.googlePayToken
                            ?: throw IllegalArgumentException("Invalid payment method - google pay token is null")
                    ),
                    BillingAddressRequest.from(paymentMethod.billingAddress),
                    CustomerDetailsRequest.from(customerDetails)
                )
            }
    }
}
