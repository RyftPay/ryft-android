package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.ui.TestData.GB_COUNTRY_CODE
import com.ryftpay.android.ui.TestData.paymentSession
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Currency

@RunWith(JUnitParamsRunner::class)
internal class TransactionInfoTest {

    private val transactionInfo = TransactionInfo(
        paymentSessionId = "ps_123",
        paymentAmount = 5940,
        currency = Currency.getInstance("GBP"),
        countryCode = GB_COUNTRY_CODE,
        paymentAmountStatus = TransactionInfo.PaymentAmountStatus.Final,
        checkoutOption = TransactionInfo.CheckoutOption.CompleteImmediatePurchase
    )

    @Test
    fun `toApiV2RequestJson should return expected transaction id`() {
        val requestJson = transactionInfo.toApiV2RequestJson()
        requestJson.get("transactionId") shouldBeEqualTo "ps_123"
    }

    @Test
    fun `toApiV2RequestJson should return expected total price`() {
        val requestJson = transactionInfo.toApiV2RequestJson()
        requestJson.get("totalPrice") shouldBeEqualTo "59.40"
    }

    @Test
    fun `toApiV2RequestJson should return expected currency code`() {
        val requestJson = transactionInfo.toApiV2RequestJson()
        requestJson.get("currencyCode") shouldBeEqualTo "GBP"
    }

    @Test
    fun `toApiV2RequestJson should return expected country code`() {
        val requestJson = transactionInfo.toApiV2RequestJson()
        requestJson.get("countryCode") shouldBeEqualTo GB_COUNTRY_CODE
    }

    @Test
    @Parameters(method = "paymentAmountStatusesToTotalPriceStatuses")
    fun `toApiV2RequestJson should return expected total price status`(
        paymentAmountStatus: TransactionInfo.PaymentAmountStatus,
        expectedTotalPriceStatus: String
    ) {
        val requestJson = transactionInfo.copy(
            paymentAmountStatus = paymentAmountStatus
        ).toApiV2RequestJson()
        requestJson.get("totalPriceStatus") shouldBeEqualTo expectedTotalPriceStatus
    }

    @Test
    @Parameters(method = "checkoutOptions")
    fun `toApiV2RequestJson should return expected checkout option`(
        checkoutOption: TransactionInfo.CheckoutOption,
        expectedCheckoutOption: String
    ) {
        val requestJson = transactionInfo.copy(
            checkoutOption = checkoutOption
        ).toApiV2RequestJson()
        requestJson.get("checkoutOption") shouldBeEqualTo expectedCheckoutOption
    }

    @Test
    fun `KEY should return expected value`() {
        TransactionInfo.KEY shouldBeEqualTo "transactionInfo"
    }

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

    private fun paymentAmountStatusesToTotalPriceStatuses(): Array<Any> = arrayOf(
        arrayOf(TransactionInfo.PaymentAmountStatus.Final, "FINAL"),
        arrayOf(TransactionInfo.PaymentAmountStatus.NotCurrentlyKnown, "NOT_CURRENTLY_KNOWN"),
        arrayOf(TransactionInfo.PaymentAmountStatus.Estimated, "ESTIMATED")
    )

    private fun checkoutOptions(): Array<Any> = arrayOf(
        arrayOf(TransactionInfo.CheckoutOption.CompleteImmediatePurchase, "COMPLETE_IMMEDIATE_PURCHASE"),
        arrayOf(TransactionInfo.CheckoutOption.Default, "DEFAULT"),
    )
}
