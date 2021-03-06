package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.TestData.merchantInfo
import com.ryftpay.android.ui.TestData.ryftTokenizationSpecification
import com.ryftpay.android.ui.TestData.transactionInfo
import org.amshove.kluent.shouldBeEqualTo
import org.json.JSONArray
import org.junit.Test

internal class LoadPaymentDataRequestTest {

    private val loadPaymentDataRequest = LoadPaymentDataRequest(
        merchantInfo,
        billingAddressRequired = false,
        ryftTokenizationSpecification,
        transactionInfo,
        emailRequired = false
    )

    private val baseApiV2Request = BaseApiRequest(
        majorApiVersion = 2,
        minorApiVersion = 0
    )

    @Test
    fun `toApiV2RequestJson returns json with api versions`() {
        val requestJson = loadPaymentDataRequest.toApiV2RequestJson(baseApiV2Request)
        requestJson.get("apiVersion") shouldBeEqualTo 2
        requestJson.get("apiVersionMinor") shouldBeEqualTo 0
    }

    @Test
    fun `toApiV2RequestJson returns json with a card payment method with input tokenization specification`() {
        val requestJson = loadPaymentDataRequest.toApiV2RequestJson(baseApiV2Request)
        requestJson.get("allowedPaymentMethods").toString() shouldBeEqualTo JSONArray()
            .put(
                CardPaymentMethod().toApiV2RequestJson(
                    billingAddressRequired = false,
                    ryftTokenizationSpecification
                )
            )
            .toString()
    }

    @Test
    fun `toApiV2RequestJson returns json with a card payment method with billing address required when it is`() {
        val requestJson = loadPaymentDataRequest.copy(
            billingAddressRequired = true
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.get("allowedPaymentMethods").toString() shouldBeEqualTo JSONArray()
            .put(
                CardPaymentMethod().toApiV2RequestJson(
                    billingAddressRequired = true,
                    ryftTokenizationSpecification
                )
            )
            .toString()
    }

    @Test
    fun `toApiV2RequestJson returns json with merchant info`() {
        val requestJson = loadPaymentDataRequest.toApiV2RequestJson(baseApiV2Request)
        requestJson.get("merchantInfo").toString() shouldBeEqualTo merchantInfo.toApiV2RequestJson().toString()
    }

    @Test
    fun `toApiV2RequestJson returns json with transaction info`() {
        val requestJson = loadPaymentDataRequest.toApiV2RequestJson(baseApiV2Request)
        requestJson.get("transactionInfo").toString() shouldBeEqualTo transactionInfo.toApiV2RequestJson().toString()
    }

    @Test
    fun `toApiV2RequestJson returns json without email required when it is not`() {
        val requestJson = loadPaymentDataRequest.copy(
            emailRequired = false
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.optBoolean("emailRequired") shouldBeEqualTo false
    }

    @Test
    fun `toApiV2RequestJson returns json with email required when it is`() {
        val requestJson = loadPaymentDataRequest.copy(
            emailRequired = true
        ).toApiV2RequestJson(baseApiV2Request)
        requestJson.getBoolean("emailRequired") shouldBeEqualTo true
    }
}
