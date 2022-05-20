package com.ryftpay.android.ui

import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.core.model.payment.PaymentSession
import com.ryftpay.android.core.model.payment.PaymentSessionStatus
import com.ryftpay.android.ui.model.RyftCardCvc
import com.ryftpay.android.ui.model.RyftCardExpiryDate
import com.ryftpay.android.ui.model.RyftCardNumber
import com.ryftpay.android.ui.model.RyftCardType
import com.ryftpay.android.ui.model.googlepay.MerchantInfo
import com.ryftpay.android.ui.model.googlepay.TokenizationSpecification
import com.ryftpay.android.ui.model.googlepay.TransactionInfo
import java.util.Currency

internal object TestData {

    private const val PAYMENT_SESSION_ID = "ps_123"
    private const val SANDBOX_PUBLIC_API_KEY_VALUE = "pk_sandbox_123"
    private const val GBP_CURRENCY_CODE = "GBP"
    private const val AMOUNT = 402

    private const val VALID_RAW_EXPIRY_DATE = "12/30"
    private const val INVALID_RAW_EXPIRY_DATE = "01/20"

    private const val VALID_VISA_RAW_CVC = "123"
    private const val INVALID_VISA_RAW_CVC = "1234"
    private const val VALID_MASTERCARD_RAW_CVC = "321"
    private const val INVALID_MASTERCARD_RAW_CVC = "4321"
    private const val VALID_AMEX_RAW_CVC = "5432"
    private const val INVALID_AMEX_RAW_CVC = "54321"

    internal const val CLIENT_SECRET = "ps_123_secret_456"
    internal const val MERCHANT_NAME = "Merchant"
    internal const val GB_COUNTRY_CODE = "GB"

    internal val validVisaRawCardNumbers = arrayOf(
        "4007 0000 0002 7",
        "4242 4242 4242 4242",
        "4137 8947 1175 5904",
        "4929 9391 8735 5598",
        "4485 3835 5028 4604",
        "4532 3078 4141 9094",
        "4716 0149 2948 1859",
        "4539 6774 9644 9015"
    )
    internal val invalidVisaRawCardNumbers = arrayOf(
        "4007 0000 1002 7",
        "4129 9391 8735 5598",
        "4485 3835 5018 4604",
        "4532 3077 4141 9094",
        "4716 0149 2940 1859",
        "4539 6724 9644 9015",
        "4539 6724 9644 90154"
    )
    internal val validMastercardRawCardNumbers = arrayOf(
        "5454 4229 5538 5717",
        "5582 0875 9468 0466",
        "5485 7276 5508 2288",
        "5523 3355 6055 0243",
        "5128 8882 8106 3960"
    )
    internal val invalidMastercardRawCardNumbers = arrayOf(
        "5454 4522 9558 5717",
        "5582 0875 9468 3466",
        "5487 7276 5508 2288",
        "5523 3355 0055 0243",
        "5128 8882 2106 3960",
        "5128 8882 2106 39607"
    )
    internal val validAmexRawCardNumbers = arrayOf(
        "3485 702508 78868",
        "3418 699947 62900",
        "3710 406105 43651",
        "3415 071516 50399",
        "3716 739213 87168"
    )
    internal val invalidAmexRawCardNumbers = arrayOf(
        "3485 702508 72868",
        "3416 699947 62900",
        "3710 406105 73651",
        "3415 571516 50399",
        "3716 739013 87168",
        "3716 739013 871689"
    )

    internal val validVisaCardNumbers = validVisaRawCardNumbers
        .toList()
        .map { RyftCardNumber.from(it) }
    internal val invalidVisaCardNumbers = invalidVisaRawCardNumbers
        .toList()
        .map { RyftCardNumber.from(it) }
    internal val validMastercardCardNumbers = validMastercardRawCardNumbers
        .toList()
        .map { RyftCardNumber.from(it) }
    internal val invalidMastercardCardNumbers = invalidMastercardRawCardNumbers
        .toList()
        .map { RyftCardNumber.from(it) }
    internal val validAmexCardNumbers = validAmexRawCardNumbers
        .toList()
        .map { RyftCardNumber.from(it) }
    internal val invalidAmexCardNumbers = invalidAmexRawCardNumbers
        .toList()
        .map { RyftCardNumber.from(it) }

    internal val validExpiryDate = RyftCardExpiryDate.from(VALID_RAW_EXPIRY_DATE)
    internal val invalidExpiryDate = RyftCardExpiryDate.from(INVALID_RAW_EXPIRY_DATE)

    internal val validVisaCvc = RyftCardCvc.from(VALID_VISA_RAW_CVC, RyftCardType.Visa)
    internal val invalidVisaCvc = RyftCardCvc.from(INVALID_VISA_RAW_CVC, RyftCardType.Visa)
    internal val validMastercardCvc = RyftCardCvc.from(VALID_MASTERCARD_RAW_CVC, RyftCardType.Mastercard)
    internal val invalidMastercardCvc = RyftCardCvc.from(INVALID_MASTERCARD_RAW_CVC, RyftCardType.Mastercard)
    internal val validAmexCvc = RyftCardCvc.from(VALID_AMEX_RAW_CVC, RyftCardType.Amex)
    internal val invalidAmexCvc = RyftCardCvc.from(INVALID_AMEX_RAW_CVC, RyftCardType.Amex)

    internal val sandboxPublicApiKey = RyftPublicApiKey(SANDBOX_PUBLIC_API_KEY_VALUE)
    internal val merchantInfo = MerchantInfo(MERCHANT_NAME)
    internal val ryftTokenizationSpecification = TokenizationSpecification.ryft(sandboxPublicApiKey)
    internal val transactionInfo = TransactionInfo(
        paymentSessionId = PAYMENT_SESSION_ID,
        paymentAmount = AMOUNT,
        currency = Currency.getInstance(GBP_CURRENCY_CODE),
        countryCode = GB_COUNTRY_CODE,
        paymentAmountStatus = TransactionInfo.PaymentAmountStatus.Final,
        checkoutOption = TransactionInfo.CheckoutOption.CompleteImmediatePurchase
    )
    internal val paymentSession = PaymentSession(
        id = PAYMENT_SESSION_ID,
        amount = AMOUNT,
        currency = Currency.getInstance(GBP_CURRENCY_CODE),
        returnUrl = "https://my-url.com",
        status = PaymentSessionStatus.PendingPayment,
        lastError = null,
        requiredAction = null,
        createdTimestamp = 1642098636,
        lastUpdatedTimestamp = 1642138419
    )
}
