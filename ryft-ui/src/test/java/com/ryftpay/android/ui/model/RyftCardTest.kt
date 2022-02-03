package com.ryftpay.android.ui.model

import com.ryftpay.android.ui.TestData.invalidAmexCardNumbers
import com.ryftpay.android.ui.TestData.invalidAmexCvc
import com.ryftpay.android.ui.TestData.invalidExpiryDate
import com.ryftpay.android.ui.TestData.invalidMastercardCardNumbers
import com.ryftpay.android.ui.TestData.invalidMastercardCvc
import com.ryftpay.android.ui.TestData.invalidVisaCardNumbers
import com.ryftpay.android.ui.TestData.invalidVisaCvc
import com.ryftpay.android.ui.TestData.validAmexCardNumbers
import com.ryftpay.android.ui.TestData.validAmexCvc
import com.ryftpay.android.ui.TestData.validExpiryDate
import com.ryftpay.android.ui.TestData.validMastercardCardNumbers
import com.ryftpay.android.ui.TestData.validMastercardCvc
import com.ryftpay.android.ui.TestData.validVisaCardNumbers
import com.ryftpay.android.ui.TestData.validVisaCvc
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
internal class RyftCardTest {

    @Test
    internal fun `Incomplete should return an incomplete card with unknown card type`() {
        val incomplete = RyftCard.Incomplete
        incomplete.number shouldBeEqualTo RyftCardNumber.Incomplete
        incomplete.expiryDate shouldBeEqualTo RyftCardExpiryDate.Incomplete
        incomplete.cvc shouldBeEqualTo RyftCardCvc.Incomplete
        incomplete.type shouldBeEqualTo RyftCardType.Unknown
        incomplete.valid shouldBeEqualTo false
    }

    @Test
    @Parameters(method = "invalidCards")
    internal fun `valid should return false if at least one field is invalid`(invalidCard: RyftCard) {
        invalidCard.valid shouldBeEqualTo false
    }

    @Test
    @Parameters(method = "validCards")
    internal fun `valid should return true if all fields are valid`(validCard: RyftCard) {
        validCard.valid shouldBeEqualTo true
    }

    @Test
    internal fun `withCardNumber should copy card with new number and type, preserving other fields`() {
        val currentCard = RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        )
        val newCard = currentCard.withCardNumber(validMastercardCardNumbers[0])
        val expectedCard = RyftCard(
            number = validMastercardCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Mastercard
        )
        newCard shouldBeEqualTo expectedCard
    }

    @Test
    internal fun `withExpiryDate should copy card with new expiry date, preserving other fields`() {
        val currentCard = RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        )
        val newCard = currentCard.withExpiryDate(invalidExpiryDate)
        val expectedCard = RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        )
        newCard shouldBeEqualTo expectedCard
    }

    @Test
    internal fun `withCvc should copy card with new cvc, preserving other fields`() {
        val currentCard = RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        )
        val newCard = currentCard.withCvc(validAmexCvc)
        val expectedCard = RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validAmexCvc,
            type = RyftCardType.Visa
        )
        newCard shouldBeEqualTo expectedCard
    }

    private fun invalidCards(): Array<RyftCard> =
        invalidVisaCards() + invalidMastercardCards() + invalidAmexCards()

    private fun invalidVisaCards(): Array<RyftCard> = arrayOf(
        RyftCard(
            number = invalidVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        ),
        RyftCard(
            number = invalidVisaCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        ),
        RyftCard(
            number = invalidVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = invalidVisaCvc,
            type = RyftCardType.Visa
        ),
        RyftCard(
            number = invalidVisaCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = invalidVisaCvc,
            type = RyftCardType.Visa
        ),
        RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        ),
        RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = invalidVisaCvc,
            type = RyftCardType.Visa
        ),
        RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = invalidVisaCvc,
            type = RyftCardType.Visa
        )
    )

    private fun invalidMastercardCards(): Array<RyftCard> = arrayOf(
        RyftCard(
            number = invalidMastercardCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validMastercardCvc,
            type = RyftCardType.Mastercard
        ),
        RyftCard(
            number = invalidVisaCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = validMastercardCvc,
            type = RyftCardType.Mastercard
        ),
        RyftCard(
            number = invalidMastercardCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = invalidMastercardCvc,
            type = RyftCardType.Mastercard
        ),
        RyftCard(
            number = invalidMastercardCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = invalidMastercardCvc,
            type = RyftCardType.Mastercard
        ),
        RyftCard(
            number = validMastercardCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = validMastercardCvc,
            type = RyftCardType.Mastercard
        ),
        RyftCard(
            number = validMastercardCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = invalidMastercardCvc,
            type = RyftCardType.Mastercard
        ),
        RyftCard(
            number = validMastercardCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = invalidMastercardCvc,
            type = RyftCardType.Mastercard
        )
    )

    private fun invalidAmexCards(): Array<RyftCard> = arrayOf(
        RyftCard(
            number = invalidAmexCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validAmexCvc,
            type = RyftCardType.Amex
        ),
        RyftCard(
            number = invalidAmexCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = validAmexCvc,
            type = RyftCardType.Amex
        ),
        RyftCard(
            number = invalidAmexCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = invalidAmexCvc,
            type = RyftCardType.Amex
        ),
        RyftCard(
            number = invalidAmexCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = invalidAmexCvc,
            type = RyftCardType.Amex
        ),
        RyftCard(
            number = validAmexCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = validAmexCvc,
            type = RyftCardType.Amex
        ),
        RyftCard(
            number = validAmexCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = invalidAmexCvc,
            type = RyftCardType.Amex
        ),
        RyftCard(
            number = validAmexCardNumbers[0],
            expiryDate = invalidExpiryDate,
            cvc = invalidAmexCvc,
            type = RyftCardType.Amex
        )
    )

    private fun validCards(): Array<RyftCard> = arrayOf(
        RyftCard(
            number = validVisaCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validVisaCvc,
            type = RyftCardType.Visa
        ),
        RyftCard(
            number = validMastercardCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validMastercardCvc,
            type = RyftCardType.Mastercard
        ),
        RyftCard(
            number = validAmexCardNumbers[0],
            expiryDate = validExpiryDate,
            cvc = validAmexCvc,
            type = RyftCardType.Amex
        )
    )
}
