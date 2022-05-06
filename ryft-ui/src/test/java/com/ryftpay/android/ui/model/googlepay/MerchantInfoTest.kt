package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.TestData.MERCHANT_NAME
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class MerchantInfoTest {

    @Test
    fun `toApiV2RequestJson should return expected json`() {
        val merchantInfo = MerchantInfo(MERCHANT_NAME)
        val requestJson = merchantInfo.toApiV2RequestJson()
        requestJson.get("merchantName") shouldBeEqualTo MERCHANT_NAME
    }

    @Test
    fun `KEY should return expected value`() {
        MerchantInfo.KEY shouldBeEqualTo "merchantInfo"
    }

    @Test
    fun `from should return expected fields`() {
        val merchantInfo = MerchantInfo.from(MERCHANT_NAME)
        val expected = MerchantInfo(MERCHANT_NAME)
        merchantInfo shouldBeEqualTo expected
    }
}
