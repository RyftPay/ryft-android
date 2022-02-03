package com.ryftpay.android.ui.extension

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// Function to only show the dialog fragment if it's not already shown
internal fun BottomSheetDialogFragment.showIfNotPresent(
    supportFragmentManager: FragmentManager,
    tagName: String
) {
    val existingFragment = supportFragmentManager.findFragmentByTag(tagName)
    if (existingFragment == null) {
        this.show(supportFragmentManager, tagName)
    }
}
