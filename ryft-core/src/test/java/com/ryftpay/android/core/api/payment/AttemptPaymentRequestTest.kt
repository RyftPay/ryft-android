package com.ryftpay.android.core.api.payment

import com.ryftpay.android.core.TestData.CLIENT_SECRET
import com.ryftpay.android.core.TestData.GOOGLE_PAY_TOKEN
import com.ryftpay.android.core.TestData.PAYMENT_METHOD_ID
import com.ryftpay.android.core.TestData.address
import com.ryftpay.android.core.TestData.cardDetails
import com.ryftpay.android.core.TestData.customerDetails
import com.ryftpay.android.core.TestData.paymentMethodOptions
import com.ryftpay.android.core.model.payment.PaymentMethod
import com.ryftpay.android.core.model.payment.PaymentMethodType
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import java.lang.IllegalArgumentException

internal class AttemptPaymentRequestTest {

    @Test
    fun `from should return expected client secret when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails = null
        ).clientSecret shouldBeEqualTo CLIENT_SECRET
    }

    @Test
    fun `from should add card details when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails = null
        ).cardDetails shouldBeEqualTo CardDetailsRequest.from(cardDetails)
    }

    @Test
    fun `from should not add wallet details when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails = null
        ).walletDetails shouldBeEqualTo null
    }

    @Test
    fun `from should not add payment method when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails = null
        ).paymentMethod shouldBeEqualTo null
    }

    @Test(expected = IllegalArgumentException::class)
    fun `from should throw exception when card payment method has no card details`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod(
                PaymentMethodType.Card,
                cardDetails = null,
                id = null,
                googlePayToken = null,
                billingAddress = null,
                options = null
            ),
            customerDetails = null
        )
    }

    @Test
    fun `from should not add billing address when card payment method has none`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
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
                id = null,
                googlePayToken = null,
                billingAddress = address,
                options = null
            ),
            customerDetails = null
        ).billingAddress shouldBeEqualTo BillingAddressRequest.from(address)
    }

    @Test
    fun `from should not add customer details when card payment method has none`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails = null
        ).customerDetails shouldBeEqualTo null
    }

    @Test
    fun `from should add customer details when card payment method has some`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails
        ).customerDetails shouldBeEqualTo CustomerDetailsRequest.from(customerDetails)
    }

    @Test
    fun `from should add default three ds request details when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails = null
        ).threeDsRequestDetails shouldBeEqualTo ThreeDsRequestDetails.Application
    }

    @Test
    fun `from should add payment method options when payment method is card`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.card(cardDetails, paymentMethodOptions),
            customerDetails = null
        ).paymentMethodOptions shouldBeEqualTo PaymentMethodOptionsRequest.from(paymentMethodOptions)
    }

    @Test
    fun `from should return expected client secret when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).clientSecret shouldBeEqualTo CLIENT_SECRET
    }

    @Test
    fun `from should not add card details when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).cardDetails shouldBeEqualTo null
    }

    @Test
    fun `from should not add wallet details when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).walletDetails shouldBeEqualTo null
    }

    @Test
    fun `from should add payment method when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).paymentMethod shouldBeEqualTo PaymentMethodRequest.from(PAYMENT_METHOD_ID)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `from should throw exception when id payment method has no card token`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod(
                PaymentMethodType.Id,
                cardDetails = null,
                id = null,
                googlePayToken = null,
                billingAddress = null,
                options = null
            ),
            customerDetails = null
        )
    }

    @Test
    fun `from should not add billing address when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).billingAddress shouldBeEqualTo null
    }

    @Test
    fun `from should not add customer details when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).customerDetails shouldBeEqualTo null
    }

    @Test
    fun `from should add default three ds request details when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).threeDsRequestDetails shouldBeEqualTo ThreeDsRequestDetails.Application
    }

    @Test
    fun `from should not add payment method options when payment method is id`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.id(id = PAYMENT_METHOD_ID),
            customerDetails = null
        ).paymentMethodOptions shouldBeEqualTo null
    }

    @Test
    fun `from should return expected client secret when payment method is google pay`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails = null
        ).clientSecret shouldBeEqualTo CLIENT_SECRET
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

    @Test
    fun `from should not add payment method when payment method is google pay`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails = null
        ).paymentMethod shouldBeEqualTo null
    }

    @Test(expected = IllegalArgumentException::class)
    fun `from should throw exception when google pay payment method has no google pay token`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod(
                PaymentMethodType.GooglePay,
                cardDetails = null,
                id = null,
                googlePayToken = null,
                billingAddress = null,
                options = null
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

    @Test
    fun `from should not add customer details when google pay payment method has none`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails = null
        ).customerDetails shouldBeEqualTo null
    }

    @Test
    fun `from should add customer details when google pay payment method has some`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails
        ).customerDetails shouldBeEqualTo CustomerDetailsRequest.from(customerDetails)
    }

    @Test
    fun `from should add default three ds request details when payment method is google pay`() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = null),
            customerDetails = null
        ).threeDsRequestDetails shouldBeEqualTo ThreeDsRequestDetails.Application
    }

    @Test
    fun `from should not add payment method options when payment method is google pay `() {
        AttemptPaymentRequest.from(
            CLIENT_SECRET,
            PaymentMethod.googlePay(GOOGLE_PAY_TOKEN, billingAddress = address),
            customerDetails = null
        ).paymentMethodOptions shouldBeEqualTo null
    }
}
