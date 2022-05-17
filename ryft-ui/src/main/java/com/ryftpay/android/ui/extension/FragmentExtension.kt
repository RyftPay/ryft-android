package com.ryftpay.android.ui.extension

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ryftpay.ui.R

internal fun Fragment.showAlertWithRetry(
    title: String? = null,
    message: String? = null,
    retryCallback: () -> Unit,
    cancelCallback: () -> Unit
) {
    AlertDialog.Builder(requireContext(), R.style.RyftAlertDialog)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(getString(R.string.ryft_retry)) { _, _ ->
            retryCallback()
        }
        .setNegativeButton(getString(R.string.ryft_cancel)) { dialog, _ ->
            dialog.dismiss()
            cancelCallback()
        }
        .setOnCancelListener {
            cancelCallback()
        }
        .create()
        .show()
}
