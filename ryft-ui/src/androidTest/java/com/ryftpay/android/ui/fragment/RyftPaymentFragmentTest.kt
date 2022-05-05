package com.ryftpay.android.ui.fragment

import android.os.Bundle
import android.view.KeyEvent
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView
import androidx.test.espresso.assertion.LayoutAssertions.noOverlaps
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch
import androidx.test.espresso.matcher.LayoutMatchers.hasEllipsizedText
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isFocusable
import androidx.test.espresso.matcher.ViewMatchers.isFocused
import androidx.test.espresso.matcher.ViewMatchers.isNotFocused
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import com.ryftpay.android.ui.component.RyftButton
import com.ryftpay.android.ui.dropin.RyftDropInConfiguration
import com.ryftpay.android.ui.dropin.RyftDropInGooglePayConfiguration
import com.ryftpay.ui.R
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RyftPaymentFragmentTest {

    @Test
    internal fun shouldShowExpectedData_OnLaunch() {
        launchFragment()

        onView(
            withId(R.id.text_ryft_payment_form_card_only_header)
        ).check(
            matches(
                allOf(isDisplayed(), withText("Credit / Debit Card"))
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            matches(
                allOf(isDisplayed(), withHint("Card number"))
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).check(
            matches(
                allOf(isDisplayed(), withHint("MM/YY"))
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                allOf(isDisplayed(), withHint("CVC"))
            )
        )

        onView(
            allOf(
                withId(R.id.text_ryft_button),
                isDescendantOfA(withId(R.id.button_ryft_pay))
            )
        ).check(
            matches(
                allOf(isDisplayed(), withText("Pay now"))
            )
        )

        onView(
            allOf(
                withId(R.id.text_ryft_button),
                isDescendantOfA(withId(R.id.button_ryft_cancel))
            )
        ).check(
            matches(
                allOf(isDisplayed(), withText("Cancel"))
            )
        )
    }

    @Test
    internal fun shouldEnableCancelAndInputFields_OnLaunch() {
        launchFragment()

        onView(
            withId(R.id.button_ryft_pay)
        ).check(
            matches(
                allOf(not(isEnabled()), not(isClickable()))
            )
        )

        onView(
            withId(R.id.button_ryft_cancel)
        ).check(
            matches(
                allOf(isEnabled(), isClickable())
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            matches(
                allOf(isEnabled(), isFocused())
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).check(
            matches(
                allOf(isEnabled(), isFocusable())
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                allOf(isEnabled(), isFocusable())
            )
        )
    }

    @Test
    internal fun shouldHaveExpectedLayout_OnLaunch() {
        launchFragment()

        assertNoLayoutBreakages()

        onView(
            withId(R.id.text_ryft_payment_form_card_only_header)
        ).check(
            isCompletelyAbove(
                withId(R.id.partial_ryft_payment_form_body)
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            isCompletelyAbove(
                withId(R.id.input_field_ryft_card_expiry_date)
            )
        )
            .check(
                isCompletelyAbove(
                    withId(R.id.input_field_ryft_card_cvc)
                )
            )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).check(
            isCompletelyLeftOf(
                withId(R.id.input_field_ryft_card_cvc)
            )
        ).check(
            isCompletelyAbove(
                withId(R.id.partial_ryft_payment_form_footer)
            )
        )

        onView(
            withId(R.id.button_ryft_pay)
        ).check(
            matches(isDisplayed())
        ).check(
            isCompletelyLeftOf(withId(R.id.button_ryft_cancel))
        )

        onView(
            withId(R.id.button_ryft_cancel)
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    internal fun shouldFocusOnFieldsAsExpected_OnLaunchAndAfterTypingText() {
        launchFragment()

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            matches(
                isFocused()
            )
        ).perform(
            typeTextIntoFocusedView("4242424242424242")
        ).check(
            matches(
                isNotFocused()
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).check(
            matches(
                isFocused()
            )
        ).perform(
            typeTextIntoFocusedView("1225"),
        ).check(
            matches(
                isNotFocused()
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                isFocused()
            )
        ).perform(
            typeTextIntoFocusedView("123"),
        ).check(
            matches(
                isNotFocused()
            )
        )
    }

    @Test
    internal fun shouldFormatFieldsAsExpected_AfterTypingText() {
        launchFragment()

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).perform(
            typeText("4242")
        ).check(
            matches(
                withText("4242 ")
            )
        ).perform(
            typeText("424242424241")
        ).check(
            matches(
                withText("4242 4242 4242 4241")
            )
        ).perform(
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL)
        ).check(
            matches(
                withText("4242 4242 4242")
            )
        ).perform(
            typeText("4242")
        ).check(
            matches(
                withText("4242 4242 4242 4242")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).perform(
            typeText("12")
        ).check(
            matches(
                withText("12/")
            )
        ).perform(
            typeText("12")
        ).check(
            matches(
                withText("12/12")
            )
        ).perform(
            pressKey(KeyEvent.KEYCODE_DEL),
            pressKey(KeyEvent.KEYCODE_DEL)
        ).check(
            matches(
                withText("12")
            )
        ).perform(
            typeText("26")
        ).check(
            matches(
                withText("12/26")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).perform(
            typeText("12")
        ).check(
            matches(
                withText("12")
            )
        ).perform(
            typeText("3")
        ).check(
            matches(
                withText("123")
            )
        )
    }

    @Test
    internal fun shouldNotAllowInvalidCharacters_WhenTypingText() {
        launchFragment()

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText(CARD_NUMBER_INVALID_CHARACTERS)
        ).check(
            matches(
                withText("")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText(CARD_EXPIRY_DATE_INVALID_CHARACTERS)
        ).check(
            matches(
                withText("")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText(CARD_CVC_INVALID_CHARACTERS)
        ).check(
            matches(
                withText("")
            )
        )
    }

    @Test
    internal fun shouldRestrictTheMaxLengthOfTheField_WhenTypingText() {
        launchFragment()

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText("4242424242424241999")
        ).check(
            matches(
                withText("4242 4242 4242 4241")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText("12023456789")
        ).check(
            matches(
                withText("12/02")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText("1234567890"),
        ).check(
            matches(
                withText("123")
            )
        )
    }

    @Test
    internal fun shouldNotTruncateCvc_WhenCardTypeIsAmex() {
        launchFragment()

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText("7373"),
        ).check(
            matches(
                withText("7373")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            matches(
                withText("")
            )
        ).perform(
            typeText("370000000000002")
        ).check(
            matches(
                withText("3700 000000 00002")
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                withText("7373")
            )
        )
    }

    @Test
    internal fun shouldEnablePay_AfterInputtingValidCardDetails() {
        launchFragment()

        inputValidCardDetails()

        onView(withId(R.id.button_ryft_pay)).check(
            matches(allOf(isEnabled(), isClickable()))
        )
    }

    @Test
    internal fun shouldChangePayButtonText_AfterClickingPay() {
        launchFragment()

        inputValidCardDetails()

        onView(withId(R.id.button_ryft_pay)).perform(click())

        onView(
            allOf(
                withId(R.id.text_ryft_button),
                isDescendantOfA(withId(R.id.button_ryft_pay))
            )
        ).check(
            matches(
                allOf(isDisplayed(), withText("Taking payment…"))
            )
        )
    }

    @Test
    internal fun shouldDisablePayAndCancelAndInputFields_AfterClickingPay() {
        launchFragment()

        inputValidCardDetails()

        onView(withId(R.id.button_ryft_pay)).perform(click())

        onView(
            withId(R.id.button_ryft_pay)
        ).check(
            matches(
                allOf(not(isEnabled()), not(isClickable()))
            )
        )

        onView(
            withId(R.id.button_ryft_cancel)
        ).check(
            matches(
                allOf(not(isEnabled()), not(isClickable()))
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_number)
        ).check(
            matches(
                allOf(not(isEnabled()))
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).check(
            matches(
                allOf(not(isEnabled()))
            )
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).check(
            matches(
                allOf(not(isEnabled()))
            )
        )
    }

    @Test
    internal fun shouldHaveExpectedLayout_AfterClickingPay() {
        launchFragment()

        inputValidCardDetails()

        onView(withId(R.id.button_ryft_pay)).perform(click())

        assertNoLayoutBreakages()

        onView(
            withId(R.id.button_ryft_pay)
        ).check(
            matches(isDisplayed())
        )

        onView(
            withId(R.id.button_ryft_cancel)
        ).check(
            matches(not(isDisplayed()))
        )
    }

    @Test
    internal fun shouldBeDismissed_AfterClickingCancel() {
        launchFragment()

        onView(isRoot()).check(matches(hasDescendant(withId(R.id.fragment_ryft_payment))))

        onView(withId(R.id.button_ryft_cancel)).perform(click())

        onView(isRoot()).check(matches(not(hasDescendant(withId(R.id.fragment_ryft_payment)))))
    }

    private fun inputValidCardDetails() {
        onView(
            withId(R.id.input_field_ryft_card_number)
        ).perform(
            typeText("4242424242424242")
        )

        onView(
            withId(R.id.input_field_ryft_card_expiry_date)
        ).perform(
            typeText("1230"),
        )

        onView(
            withId(R.id.input_field_ryft_card_cvc)
        ).perform(
            typeText("123"),
        )
    }

    private fun launchFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInContainer(createFragmentArgs()) {
            RyftPaymentFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
    }

    private fun createFragmentArgs(): Bundle {
        val configuration = RyftDropInConfiguration(
            clientSecret = "secret_123",
            subAccountId = null,
            googlePayConfiguration = RyftDropInGooglePayConfiguration(
                merchantName = "Ryft",
                merchantCountryCode = "GB"
            )
        )
        val publicApiKey = RyftPublicApiKey("pk_sandbox_123")
        return bundleOf(
            RyftPaymentFragment.ARGUMENTS_BUNDLE_KEY
                to RyftPaymentFragment.Arguments(configuration, publicApiKey, testMode = true)
        )
    }

    private fun assertNoLayoutBreakages() {
        onView(isRoot()).check(noOverlaps())
        onView(isRoot()).check(
            selectedDescendantsMatch(
                isAssignableFrom(RyftButton::class.java), not(hasEllipsizedText())
            )
        )
    }

    companion object {
        private const val LETTERS = "abcdefghijklmnopqrstuwxyz"
        private const val TYPE_TEXT_ALLOWED_SYMBOLS = "`\"-_=+[{]};:'~,<.>/?\\| "
        private const val NON_DIGIT_CHARACTERS = LETTERS + TYPE_TEXT_ALLOWED_SYMBOLS
        private const val CARD_NUMBER_INVALID_CHARACTERS = NON_DIGIT_CHARACTERS
        private const val CARD_EXPIRY_DATE_INVALID_CHARACTERS = NON_DIGIT_CHARACTERS
        private const val CARD_CVC_INVALID_CHARACTERS = NON_DIGIT_CHARACTERS
    }
}
