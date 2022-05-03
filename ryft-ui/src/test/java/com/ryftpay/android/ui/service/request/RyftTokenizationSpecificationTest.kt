package com.ryftpay.android.ui.service.request

import com.ryftpay.android.ui.TestData.sandboxPublicApiKey
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftTokenizationSpecificationTest {

    @Test
    fun `from should return expected fields`() {
        val tokenizationSpecification = RyftTokenizationSpecification.from(sandboxPublicApiKey)
        val expected = RyftTokenizationSpecification(sandboxPublicApiKey)
        tokenizationSpecification shouldBeEqualTo expected
    }
}
