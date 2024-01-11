package com.ryftpay.android.ui.model.threeds

import com.checkout.threeds.domain.model.AuthenticationCompleted
import com.checkout.threeds.domain.model.AuthenticationResult
import com.checkout.threeds.domain.model.ResultType
import io.mockk.every
import io.mockk.mockk
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class ThreeDsIdentificationResultTest {

    @Test
    internal fun `fromCheckoutResult should return expected result when AuthenticationError`() {
        val authenticationResult = mockk<AuthenticationResult>(relaxed = true)
        every {
            authenticationResult.resultType
        } answers {
            ResultType.Error
        }

        ThreeDsIdentificationResult.fromCheckoutResult(
            authenticationResult
        ) shouldBeEqualTo ThreeDsIdentificationResult.Error
    }

    @Test
    @Parameters(method = "checkoutThreeDsTransactionStatusesToRyftThreeDsResults")
    internal fun `fromCheckoutResult should return expected result when AuthenticationCompleted`(
        transactionStatus: String,
        expected: ThreeDsIdentificationResult
    ) {
        val authenticationCompleted = mockk<AuthenticationCompleted>(relaxed = true)
        every {
            authenticationCompleted.transactionStatus
        } answers {
            transactionStatus
        }

        ThreeDsIdentificationResult.fromCheckoutResult(
            authenticationCompleted
        ) shouldBeEqualTo expected
    }

    private fun checkoutThreeDsTransactionStatusesToRyftThreeDsResults(): Array<Any> = arrayOf(
        arrayOf("N", ThreeDsIdentificationResult.Fail),
        arrayOf("U", ThreeDsIdentificationResult.Fail),
        arrayOf("C", ThreeDsIdentificationResult.Fail),
        arrayOf("D", ThreeDsIdentificationResult.Fail),
        arrayOf("R", ThreeDsIdentificationResult.Fail),
        arrayOf("A", ThreeDsIdentificationResult.Success),
        arrayOf("I", ThreeDsIdentificationResult.Success),
        arrayOf("Y", ThreeDsIdentificationResult.Success)
    )
}
