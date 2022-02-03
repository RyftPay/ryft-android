package com.ryftpay.android.ui.extension

import android.view.View
import com.ryftpay.android.ui.listener.OnSingleClickListener

// Function to override on click listener with custom listener preventing fast double clicks
internal fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}
