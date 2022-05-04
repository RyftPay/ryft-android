package com.ryftpay.android.ui.service.request.googlepay

import com.ryftpay.android.ui.TestData.GB_COUNTRY_CODE
import com.ryftpay.android.ui.TestData.paymentSession
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class TransactionInfoTest {

    @Test
    fun `from should return expected fields`() {
        val transactionInfo = TransactionInfo.from(
            paymentSession,
            GB_COUNTRY_CODE
        )
        val expected = TransactionInfo(
            paymentSessionId = paymentSession.id,
            paymentAmount = paymentSession.amount,
            currency = paymentSession.currency,
            countryCode = GB_COUNTRY_CODE,
            paymentAmountStatus = TransactionInfo.PaymentAmountStatus.Final,
            checkoutOption = TransactionInfo.CheckoutOption.CompleteImmediatePurchase
        )
        transactionInfo shouldBeEqualTo expected
    }
}
