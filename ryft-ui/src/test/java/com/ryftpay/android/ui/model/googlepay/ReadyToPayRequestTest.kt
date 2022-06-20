package com.ryftpay.android.ui.model.googlepay

import org.amshove.kluent.shouldBeEqualTo
import org.json.JSONArray
import org.junit.Test

internal class ReadyToPayRequestTest {

    private val baseApiV2Request = BaseApiRequest(
        majorApiVersion = 2,
        minorApiVersion = 0
    )

    @Test
    fun `toApiV2RequestJson returns json with api versions`() {
        val requestJson = ReadyToPayRequest(
            existingPaymentMethodRequired = false,
            billingAddressRequired = false
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.getInt("apiVersion") shouldBeEqualTo 2
        requestJson.getInt("apiVersionMinor") shouldBeEqualTo 0
    }

    @Test
    fun `toApiV2RequestJson returns json with a card payment method with no tokenization specification`() {
        val requestJson = ReadyToPayRequest(
            existingPaymentMethodRequired = false,
            billingAddressRequired = false
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.get("allowedPaymentMethods").toString() shouldBeEqualTo JSONArray()
            .put(
                CardPaymentMethod().toApiV2RequestJson(
                    billingAddressRequired = false,
                    tokenizationSpecification = null
                )
            )
            .toString()
    }

    @Test
    fun `toApiV2RequestJson returns json with a card payment method with billing address required when it is`() {
        val requestJson = ReadyToPayRequest(
            existingPaymentMethodRequired = false,
            billingAddressRequired = true
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.get("allowedPaymentMethods").toString() shouldBeEqualTo JSONArray()
            .put(
                CardPaymentMethod().toApiV2RequestJson(
                    billingAddressRequired = true,
                    tokenizationSpecification = null
                )
            )
            .toString()
    }

    @Test
    fun `toApiV2RequestJson returns json with existingPaymentMethodRequired when it is true`() {
        val requestJson = ReadyToPayRequest(
            billingAddressRequired = false,
            existingPaymentMethodRequired = true
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.getBoolean("existingPaymentMethodRequired") shouldBeEqualTo true
    }

    @Test
    fun `toApiV2RequestJson returns json without existingPaymentMethodRequired when it is false`() {
        val requestJson = ReadyToPayRequest(
            billingAddressRequired = false,
            existingPaymentMethodRequired = false
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.optBoolean("existingPaymentMethodRequired") shouldBeEqualTo false
    }
}
