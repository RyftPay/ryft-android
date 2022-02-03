package com.ryftpay.android.ui.component.watcher

import android.text.Editable
import android.text.TextWatcher

// TextWatcher that provides the action the user performed to the text on top of what changed
internal abstract class AfterTextWatcherWithAction : TextWatcher {

    protected var currentAction = TextWatcherAction.Unknown

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        currentAction = TextWatcherAction.from(
            textLengthToBeDeleted = count,
            textLengthToBeAdded = after
        )
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged(s?.toString(), currentAction)
    }

    abstract fun afterTextChanged(text: String?, action: TextWatcherAction)
}
