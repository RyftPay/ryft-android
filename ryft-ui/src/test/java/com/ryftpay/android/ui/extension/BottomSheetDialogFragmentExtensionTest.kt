package com.ryftpay.android.ui.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class BottomSheetDialogFragmentExtensionTest {

    private val supportFragmentManager = mockk<FragmentManager>(relaxed = true)
    private val fragment = mockk<Fragment>(relaxed = true)
    private val bottomSheetDialogFragment = BottomSheetDialogFragment()
    private val fragmentTagName = "Test"

    @Test
    internal fun `BottomSheetDialogFragment should not be shown if one exists with the same name`() {
        findFragmentByTagReturns(fragment)

        bottomSheetDialogFragment.showIfNotPresent(supportFragmentManager, fragmentTagName)

        verify(exactly = 0) {
            bottomSheetDialogFragment.show(supportFragmentManager, fragmentTagName)
        }
    }

    @Test
    internal fun `BottomSheetDialogFragment should be shown if none exist with the same name`() {
        findFragmentByTagReturns(fragment = null)

        bottomSheetDialogFragment.showIfNotPresent(supportFragmentManager, fragmentTagName)

        verify {
            bottomSheetDialogFragment.show(supportFragmentManager, fragmentTagName)
        }
    }

    private fun findFragmentByTagReturns(fragment: Fragment?) {
        every {
            supportFragmentManager.findFragmentByTag(fragmentTagName)
        } answers {
            fragment
        }
    }
}
