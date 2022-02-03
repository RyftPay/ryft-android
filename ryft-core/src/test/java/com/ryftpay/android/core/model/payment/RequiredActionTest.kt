package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.TestData.requiredActionResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RequiredActionTest {

    @Test
    fun `from should return expected fields`() {
        val requiredAction = RequiredAction.from(requiredActionResponse)
        requiredAction.type shouldBeEqualTo RequiredActionType.from(requiredActionResponse.type)
        requiredAction.url shouldBeEqualTo requiredActionResponse.url
    }
}
