package com.ryftpay.android.ui.component.watcher

internal enum class TextWatcherAction {
    Insert,
    Delete,
    Unknown;

    companion object {
        internal fun from(textLengthToBeDeleted: Int, textLengthToBeAdded: Int) = when {
            textLengthToBeAdded > textLengthToBeDeleted -> Insert
            textLengthToBeDeleted > textLengthToBeAdded -> Delete
            else -> Unknown
        }
    }
}
