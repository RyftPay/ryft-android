package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.TestData.sandboxPublicApiKey
import com.ryftpay.android.ui.model.googlepay.RyftTokenizationSpecification
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
