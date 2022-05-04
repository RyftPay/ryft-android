package com.ryftpay.android.ui.service.request.googlepay

import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentDataRequest
import com.ryftpay.android.ui.extension.formatPriceWithoutCurrencySymbol
import com.ryftpay.android.ui.model.RyftCardType
import org.json.JSONArray
import org.json.JSONObject
import java.util.Currency

internal object GooglePayRequestFactory {

    private const val MAJOR_API_VERSION = 2
    private const val MINOR_API_VERSION = 0
    private const val CARD_PAYMENT_METHOD = "CARD"
    private const val IS_EXISTING_PAYMENT_METHOD_REQUIRED = true
    private const val PAYMENT_GATEWAY_TOKENIZATION_SPECIFICATION_TYPE = "PAYMENT_GATEWAY"
    private const val RYFT_PAYMENT_GATEWAY = "ryft"
    private val supportedAuthMethods = arrayOf("PAN_ONLY", "CRYPTOGRAM_3DS")
    private val baseRequest: JSONObject = JSONObject()
        .put("apiVersion", MAJOR_API_VERSION)
        .put("apiVersionMinor", MINOR_API_VERSION)
    private val cardPaymentMethod: JSONObject = JSONObject()
        .put("type", CARD_PAYMENT_METHOD)
        .put(
            "parameters",
            JSONObject()
                .put("allowedAuthMethods", JSONArray(supportedAuthMethods))
                .put(
                    "allowedCardNetworks",
                    JSONArray(
                        RyftCardType.getGooglePaySupportedTypeNames()
                    )
                )
        )

    internal val isReadyToPayRequest: IsReadyToPayRequest = IsReadyToPayRequest.fromJson(
        JSONObject(baseRequest.toString()).apply {
            put("existingPaymentMethodRequired", IS_EXISTING_PAYMENT_METHOD_REQUIRED)
            put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
        }.toString()
    )

    internal fun createPaymentDataRequest(
        loadPaymentDataRequest: LoadPaymentDataRequest
    ): PaymentDataRequest = PaymentDataRequest.fromJson(
        JSONObject(baseRequest.toString()).apply {
            put("merchantInfo", createMerchantInfo(loadPaymentDataRequest.merchantInfo))
            put(
                "allowedPaymentMethods",
                JSONArray().put(
                    createCardPaymentMethodWithTokenizationSpecification(
                        loadPaymentDataRequest.tokenizationSpecification
                    )
                )
            )
            put(
                "transactionInfo",
                createTransactionInfo(loadPaymentDataRequest.transactionInfo)
            )
        }.toString()
    )

    private fun createMerchantInfo(
        merchantInfo: MerchantInfo
    ): JSONObject = JSONObject()
        .put("merchantName", merchantInfo.displayName)

    private fun createTokenizationSpecification(
        tokenizationSpecification: RyftTokenizationSpecification
    ): JSONObject = JSONObject()
        .put("type", PAYMENT_GATEWAY_TOKENIZATION_SPECIFICATION_TYPE)
        .put(
            "parameters",
            JSONObject()
                .put("gateway", RYFT_PAYMENT_GATEWAY)
                .put("gatewayMerchantId", tokenizationSpecification.publicApiKey.value)
        )

    private fun createCardPaymentMethodWithTokenizationSpecification(
        tokenizationSpecification: RyftTokenizationSpecification
    ): JSONObject =
        JSONObject(cardPaymentMethod.toString()).apply {
            put(
                "tokenizationSpecification",
                createTokenizationSpecification(tokenizationSpecification)
            )
        }

    private fun createTransactionInfo(
        transactionInfo: TransactionInfo
    ): JSONObject = JSONObject()
        .put("transactionId", transactionInfo.paymentSessionId)
        .put(
            "totalPrice",
            getTotalPrice(transactionInfo.paymentAmount, transactionInfo.currency)
        )
        .put(
            "totalPriceStatus",
            getTotalPriceStatus(transactionInfo.paymentAmountStatus)
        )
        .put(
            "checkoutOption",
            getCheckoutOption(transactionInfo.checkoutOption)
        )
        .put("countryCode", transactionInfo.countryCode)
        .put("currencyCode", transactionInfo.currency.currencyCode)

    private fun getTotalPrice(
        paymentAmount: Int,
        currency: Currency
    ): String = paymentAmount.formatPriceWithoutCurrencySymbol(currency)

    private fun getTotalPriceStatus(
        paymentAmountStatus: TransactionInfo.PaymentAmountStatus
    ): String =
        when (paymentAmountStatus) {
            TransactionInfo.PaymentAmountStatus.Final -> "FINAL"
            TransactionInfo.PaymentAmountStatus.Estimated -> "ESTIMATED"
            TransactionInfo.PaymentAmountStatus.NotCurrentlyKnown -> "NOT_CURRENTLY_KNOWN"
        }

    private fun getCheckoutOption(
        checkoutOption: TransactionInfo.CheckoutOption
    ): String =
        when (checkoutOption) {
            TransactionInfo.CheckoutOption.Default -> "DEFAULT"
            TransactionInfo.CheckoutOption.CompleteImmediatePurchase -> "COMPLETE_IMMEDIATE_PURCHASE"
        }
}
