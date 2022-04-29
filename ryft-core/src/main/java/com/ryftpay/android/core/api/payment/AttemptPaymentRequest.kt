package com.ryftpay.android.core.api.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentMethodType
import java.lang.IllegalArgumentException

data class AttemptPaymentRequest(
    @JsonProperty("clientSecret") val clientSecret: String,
    @JsonProperty("cardDetails") val cardDetails: CardDetailsRequest?,
    @JsonProperty("walletDetails") val walletDetails: WalletDetailsRequest?
) {
    companion object {
        internal fun from(clientSecret: String, paymentMethod: PaymentMethod): AttemptPaymentRequest =
            when (paymentMethod.type) {
                PaymentMethodType.Card -> AttemptPaymentRequest(
                    clientSecret = clientSecret,
                    cardDetails = CardDetailsRequest.from(
                        paymentMethod.cardDetails
                            ?: throw IllegalArgumentException("Invalid payment method - card details is null")
                    ),
                    walletDetails = null
                )
                PaymentMethodType.GooglePay -> AttemptPaymentRequest(
                    clientSecret = clientSecret,
                    cardDetails = null,
                    walletDetails = WalletDetailsRequest.from(
                        paymentMethod.googlePayToken
                            ?: throw IllegalArgumentException("Invalid payment method - google pay token is null")
                    )
                )
            }
    }
}
