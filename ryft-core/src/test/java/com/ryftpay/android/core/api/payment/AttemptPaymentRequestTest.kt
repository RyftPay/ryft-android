package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.CLIENT_SECRET
import com.ryftpay.android.core.TestData.GOOGLE_PAY_TOKEN
import com.ryftpay.android.core.TestData.address
import com.ryftpay.android.core.TestData.cardDetails
import com.ryftpay.android.core.TestData.customerDetails
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentMethodType
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.lang.IllegalArgumentException

internal class AttemptPaymentRequestTest {

    @Test
    fun `from should return expected client secret`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails),
            customerDetails = null
        ).clientSecret shouldBeEqualTo CLIENT_SECRET
    }

    @Test
    fun `from should add card details when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails),
            customerDetails = null
        ).cardDetails shouldBeEqualTo CardDetailsRequest.from(cardDetails)
    }

    @Test
    fun `from should not add wallet details when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails),
            customerDetails = null
        ).walletDetails shouldBeEqualTo null
    }

    @Test(expected = IllegalArgumentException::class)
    fun `from should throw exception when card payment method has no card details`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod(
                PaymentMethodType.Card,
                cardDetails = null,
                googlePayToken = null,
                billingAddress = null
            ),
            customerDetails = null
        )
    }

    @Test
    fun `from should not add billing address when card payment method has none`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails),
            customerDetails = null
        ).billingAddress shouldBeEqualTo null
    }

    @Test
    fun `from should add billing address when card payment method has one`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod(
                type = PaymentMethodType.Card,
                cardDetails,
                googlePayToken = null,
                billingAddress = address
            ),
            customerDetails = null
        ).billingAddress shouldBeEqualTo BillingAddressRequest.from(address)
    }

    @Test
    fun `from should not add customer details when they are null`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails),
            customerDetails = null
        ).customerDetails shouldBeEqualTo null
    }

    @Test
    fun `from should add customer details when they are provided`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails),
            customerDetails
        ).customerDetails shouldBeEqualTo CustomerDetailsRequest.from(customerDetails)
    }

    @Test
    fun `from should add wallet details when payment method is google pay`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails = null
        ).walletDetails shouldBeEqualTo WalletDetailsRequest.from(GOOGLE_PAY_TOKEN)
    }

    @Test
    fun `from should not add card details when payment method is google pay`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails = null
        ).cardDetails shouldBeEqualTo null
    }

    @Test(expected = IllegalArgumentException::class)
    fun `from should throw exception when google pay payment method has no google pay token`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod(
                PaymentMethodType.GooglePay,
                cardDetails = null,
                googlePayToken = null,
                billingAddress = null
            ),
            customerDetails = null
        )
    }

    @Test
    fun `from should not add billing address when google pay payment method has none`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails = null
        ).billingAddress shouldBeEqualTo null
    }

    @Test
    fun `from should add billing address when google pay payment method has one`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = address),
            customerDetails = null
        ).billingAddress shouldBeEqualTo BillingAddressRequest.from(address)
    }
}
