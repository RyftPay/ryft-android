package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.TestData.sandboxPublicApiKey
import org.amshove.kluent.shouldBeEqualTo
import org.json.JSONObject
import org.junit.Test

internal class TokenizationSpecificationTest {

    private val tokenizationSpecification = TokenizationSpecification(
        gateway = "a gateway",
        gatewayMerchantId = "pk_123"
    )

    @Test
    fun `toApiV2RequestJson should return expected type`() {
        val requestJson = tokenizationSpecification.toApiV2RequestJson()
        requestJson.get("type") shouldBeEqualTo "PAYMENT_GATEWAY"
    }

    @Test
    fun `toApiV2RequestJson should return expected parameters`() {
        val requestJson = tokenizationSpecification.toApiV2RequestJson()
        requestJson.get("parameters").toString() shouldBeEqualTo JSONObject()
            .put("gateway", "a gateway")
            .put("gatewayMerchantId", "pk_123")
            .toString()
    }

    @Test
    fun `KEY should return expected value`() {
        TokenizationSpecification.KEY shouldBeEqualTo "tokenizationSpecification"
    }

    @Test
    fun `ryft should return expected fields`() {
        val tokenizationSpecification = TokenizationSpecification.ryft(sandboxPublicApiKey)
        val expected = TokenizationSpecification(
            gateway = "ryft",
            gatewayMerchantId = sandboxPublicApiKey.value
        )
        tokenizationSpecification shouldBeEqualTo expected
    }
}
