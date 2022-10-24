package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.identifyRequiredActionResponse
import com.ryftpay.android.core.TestData.redirectRequiredActionResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RequiredActionTest {

    @Test
    fun `from should return expected fields when response is redirect type`() {
        val requiredAction = RequiredAction.from(redirectRequiredActionResponse)
        requiredAction.type shouldBeEqualTo RequiredActionType.from(redirectRequiredActionResponse.type)
        requiredAction.url shouldBeEqualTo redirectRequiredActionResponse.url
        requiredAction.identify shouldBeEqualTo null
    }

    @Test
    fun `from should return expected fields when response is identify type`() {
        val requiredAction = RequiredAction.from(identifyRequiredActionResponse)
        requiredAction.type shouldBeEqualTo RequiredActionType.from(identifyRequiredActionResponse.type)
        requiredAction.url shouldBeEqualTo null
        requiredAction.identify shouldBeEqualTo IdentifyAction.from(identifyRequiredActionResponse.identify!!)
    }
}
