package com.ryftpay.android.ui.extension

import com.checkout.threeds.Environment
import com.google.android.gms.wallet.WalletConstants
import com.ryftpay.android.core.model.api.RyftEnvironment
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RyftEnvironmentExtensionTest {

    @Test
    @Parameters(method = "ryftEnvironmentsToGooglePayEnvironments")
    internal fun `toGooglePayEnvironment should return expected integer`(
        ryftEnvironment: RyftEnvironment,
        expected: Int
    ) {
        ryftEnvironment.toGooglePayEnvironment() shouldBeEqualTo expected
    }

    @Test
    @Parameters(method = "ryftEnvironmentsToCheckoutComEnvironments")
    internal fun `toCheckoutComEnvironment should return expected environment`(
        ryftEnvironment: RyftEnvironment,
        expected: Environment
    ) {
        ryftEnvironment.toCheckoutComEnvironment() shouldBeEqualTo expected
    }

    private fun ryftEnvironmentsToGooglePayEnvironments(): Array<Any> = arrayOf(
        arrayOf(RyftEnvironment.Prod, WalletConstants.ENVIRONMENT_PRODUCTION),
        arrayOf(RyftEnvironment.Sandbox, WalletConstants.ENVIRONMENT_TEST)
    )

    private fun ryftEnvironmentsToCheckoutComEnvironments(): Array<Any> = arrayOf(
        arrayOf(RyftEnvironment.Prod, Environment.PRODUCTION),
        arrayOf(RyftEnvironment.Sandbox, Environment.SANDBOX)
    )
}
