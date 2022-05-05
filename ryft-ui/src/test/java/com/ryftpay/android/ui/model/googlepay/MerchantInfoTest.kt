package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.TestData.MERCHANT_NAME
import com.ryftpay.android.ui.model.googlepay.MerchantInfo
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class MerchantInfoTest {

    @Test
    fun `from should return expected fields`() {
        val merchantInfo = MerchantInfo.from(MERCHANT_NAME)
        val expected = MerchantInfo(MERCHANT_NAME)
        merchantInfo shouldBeEqualTo expected
    }
}
