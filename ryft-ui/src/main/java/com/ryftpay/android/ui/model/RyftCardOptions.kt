package com.ryftpay.android.ui.model

internal data class RyftCardOptions(
    internal val saveForFuture: Boolean
) {
    internal fun withSaveForFuture(saveForFuture: Boolean): RyftCardOptions = copy(
        saveForFuture = saveForFuture
    )

    companion object {
        internal val Default = RyftCardOptions(saveForFuture = false)
    }
}
