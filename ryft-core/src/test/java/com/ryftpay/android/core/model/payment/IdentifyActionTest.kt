package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.identifyActionResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class IdentifyActionTest {

    @Test
    fun `from should return expected values`() {
        val identifyAction = IdentifyAction.from(identifyActionResponse)
        identifyAction.scheme shouldBeEqualTo identifyActionResponse.scheme
        identifyAction.paymentMethodId shouldBeEqualTo identifyActionResponse.paymentMethodId
        identifyAction.protocolVersion shouldBeEqualTo identifyActionResponse.protocolVersion
    }
}
