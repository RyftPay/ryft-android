package com.ryftpay.android.ui.extension

import android.text.TextWatcher
import android.widget.EditText

// Set the text of an edittext within a TextWatcher without triggering TextWatcher events
internal fun TextWatcher.setText(editText: EditText, text: String) {
    editText.removeTextChangedListener(this)
    editText.setText(text)
    editText.setSelection(text.length)
    editText.addTextChangedListener(this)
}
