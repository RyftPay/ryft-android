package com.ryftpay.android.ui.model.googlepay

import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.ui.extension.formatPriceWithoutCurrencySymbol
import org.json.JSONObject
import java.util.Currency

internal data class TransactionInfo(
    val paymentSessionId: String,
    val paymentAmount: Int,
    val currency: Currency,
    val countryCode: String,
    val paymentAmountStatus: PaymentAmountStatus,
    val checkoutOption: CheckoutOption
) {

    internal enum class PaymentAmountStatus(
        internal val googlePayName: String
    ) {
        NotCurrentlyKnown(googlePayName = "NOT_CURRENTLY_KNOWN"),
        Estimated(googlePayName = "ESTIMATED"),
        Final(googlePayName = "FINAL")
    }

    internal enum class CheckoutOption(
        internal val googlePayName: String
    ) {
        Default(googlePayName = "DEFAULT"),
        CompleteImmediatePurchase(googlePayName = "COMPLETE_IMMEDIATE_PURCHASE")
    }

    internal fun toApiV2RequestJson(): JSONObject = JSONObject()
        .put(TRANSACTION_ID_KEY, paymentSessionId)
        .put(TOTAL_PRICE_KEY, paymentAmount.formatPriceWithoutCurrencySymbol(currency))
        .put(TOTAL_PRICE_STATUS_KEY, paymentAmountStatus.googlePayName)
        .put(CHECKOUT_OPTION_KEY, checkoutOption.googlePayName)
        .put(COUNTRY_CODE_KEY, countryCode)
        .put(CURRENCY_CODE_KEY, currency.currencyCode)

    companion object {
        internal const val KEY = "transactionInfo"
        private const val TRANSACTION_ID_KEY = "transactionId"
        private const val TOTAL_PRICE_KEY = "totalPrice"
        private const val TOTAL_PRICE_STATUS_KEY = "totalPriceStatus"
        private const val CHECKOUT_OPTION_KEY = "checkoutOption"
        private const val COUNTRY_CODE_KEY = "countryCode"
        private const val CURRENCY_CODE_KEY = "currencyCode"

        internal fun from(
            paymentSession: PaymentSession,
            countryCode: String,
        ) = TransactionInfo(
            paymentSession.id,
            paymentSession.amount,
            paymentSession.currency,
            countryCode,
            PaymentAmountStatus.Final,
            CheckoutOption.CompleteImmediatePurchase
        )
    }
}
