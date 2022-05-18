package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.TestData.ryftTokenizationSpecification
import org.amshove.kluent.shouldBeEqualTo
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.Test

internal class CardPaymentMethodTest {

    @Test
    fun `toApiV2RequestJson should return expected type`() {
        val requestJson = CardPaymentMethod().toApiV2RequestJson(
            billingAddressRequired = false,
            tokenizationSpecification = null
        )
        requestJson.get("type") shouldBeEqualTo "CARD"
    }

    @Test
    fun `toApiV2RequestJson should return expected parameters when billing address is not required`() {
        val requestJson = CardPaymentMethod().toApiV2RequestJson(
            billingAddressRequired = false,
            tokenizationSpecification = null
        )
        requestJson.get("parameters").toString() shouldBeEqualTo JSONObject()
            .put(
                "allowedAuthMethods",
                JSONArray()
                    .put("PAN_ONLY")
                    .put("CRYPTOGRAM_3DS")
            )
            .put(
                "allowedCardNetworks",
                JSONArray()
                    .put("VISA")
                    .put("MASTERCARD")
            )
            .toString()
    }

    @Test
    fun `toApiV2RequestJson should return expected parameters when billing address is required`() {
        val requestJson = CardPaymentMethod().toApiV2RequestJson(
            billingAddressRequired = true,
            tokenizationSpecification = null
        )
        requestJson.get("parameters").toString() shouldBeEqualTo JSONObject()
            .put(
                "allowedAuthMethods",
                JSONArray()
                    .put("PAN_ONLY")
                    .put("CRYPTOGRAM_3DS")
            )
            .put(
                "allowedCardNetworks",
                JSONArray()
                    .put("VISA")
                    .put("MASTERCARD")
            )
            .put("billingAddressRequired", true)
            .put(
                "billingAddressParameters",
                JSONObject()
                    .put("format", "FULL")
            )
            .toString()
    }

    @Test(expected = JSONException::class)
    fun `toApiV2RequestJson should not return tokenization specification when null`() {
        val requestJson = CardPaymentMethod().toApiV2RequestJson(
            billingAddressRequired = false,
            tokenizationSpecification = null
        )
        requestJson.get("tokenizationSpecification")
    }

    @Test
    fun `toApiV2RequestJson should return tokenization specification when provided`() {
        val requestJson = CardPaymentMethod().toApiV2RequestJson(
            billingAddressRequired = false,
            ryftTokenizationSpecification
        )
        val expected = ryftTokenizationSpecification.toApiV2RequestJson().toString()
        requestJson.get("tokenizationSpecification").toString() shouldBeEqualTo expected
    }
}
