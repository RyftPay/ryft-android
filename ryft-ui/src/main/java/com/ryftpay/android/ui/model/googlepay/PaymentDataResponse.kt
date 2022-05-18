package com.ryftpay.android.ui.model.googlepay

import com.google.android.gms.wallet.PaymentData
import com.ryftpay.android.core.model.payment.Address
import com.ryftpay.android.ui.extension.extractFirstAndLastNamesOrNulls
import org.json.JSONObject

internal data class PaymentDataResponse(
    val token: String,
    val billingAddress: Address?
) {
    companion object {
        internal fun from(paymentData: PaymentData): PaymentDataResponse {
            val json = JSONObject(paymentData.toJson())
            val paymentMethodJson = json.getJSONObject("paymentMethodData")
            val cardInfoJson = paymentMethodJson.getJSONObject("info")
            val billingAddressJson = cardInfoJson.optJSONObject("billingAddress")
            val token = paymentMethodJson
                .getJSONObject("tokenizationData")
                .get("token")
                .toString()
            return PaymentDataResponse(token, addressFrom(billingAddressJson))
        }

        private fun addressFrom(addressJson: JSONObject?): Address? {
            if (addressJson == null) {
                return null
            }
            val firstAndLastNames = addressJson
                .optString("name")
                .extractFirstAndLastNamesOrNulls()
            return Address(
                firstName = firstAndLastNames.first,
                lastName = firstAndLastNames.second,
                lineOne = addressJson.getString("address1").ifEmpty { null },
                lineTwo = addressJson.optString("address2").ifEmpty { null },
                city = addressJson.getString("locality").ifEmpty { null },
                country = addressJson.getString("countryCode"),
                postalCode = addressJson.getString("postalCode"),
                region = addressJson.optString("administrativeArea").ifEmpty { null }
            )
        }
    }
}
