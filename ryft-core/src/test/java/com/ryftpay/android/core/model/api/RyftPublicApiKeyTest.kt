package com.ryftpay.android.core.model.api

import com.ryftpay.android.core.TestData.PROD_PUBLIC_API_KEY_VALUE
import com.ryftpay.android.core.TestData.SANDBOX_PUBLIC_API_KEY_VALUE
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldThrow
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException
import java.util.UUID

@RunWith(JUnitParamsRunner::class)
internal class RyftPublicApiKeyTest {

    @Test
    @Parameters(method = "invalidPublicKeys")
    fun `Constructor should reject invalid public api keys`(key: String) {
        invoking { RyftPublicApiKey(key) } shouldThrow IllegalArgumentException::class
    }

    @Test
    @Parameters(method = "validPublicKeys")
    fun `Constructor should accept valid public api keys`(key: String) {
        invoking { RyftPublicApiKey(key) } shouldNotThrow IllegalArgumentException::class
    }

    @Test
    @Parameters(method = "validPublicKeysToExpectedEnvironments")
    fun `getEnvironment should return Sandbox environment for Sandbox public api key`(key: String, expected: RyftEnvironment) {
        RyftPublicApiKey(key).getEnvironment() shouldBeEqualTo expected
    }

    private fun invalidPublicKeys(): Array<String> = arrayOf(
        "",
        "       ",
        "ij4rij4rofk-00vjf",
        UUID.randomUUID().toString(),
        "sk_sandbox_123",
        "sk_123"
    )

    private fun validPublicKeys(): Array<String> = arrayOf(
        SANDBOX_PUBLIC_API_KEY_VALUE,
        PROD_PUBLIC_API_KEY_VALUE
    )

    private fun validPublicKeysToExpectedEnvironments(): Array<Any> = arrayOf(
        arrayOf(SANDBOX_PUBLIC_API_KEY_VALUE, RyftEnvironment.Sandbox),
        arrayOf(PROD_PUBLIC_API_KEY_VALUE, RyftEnvironment.Prod)
    )
}
