package com.ryftpay.android.ui.listener

import android.view.View

// Listener to prevent fast double clicks causing multiple events
internal class OnSingleClickListener(
    listener: (View) -> Unit
) : View.OnClickListener {

    private val onClickListener: View.OnClickListener =
        View.OnClickListener { listener.invoke(it) }

    override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()

        if (previousClickWithinDelay(currentTimeMillis)) {
            previousClickTimeMillis = currentTimeMillis
            onClickListener.onClick(v)
        }
    }

    private fun previousClickWithinDelay(currentTimeMillis: Long) =
        currentTimeMillis >= previousClickTimeMillis + DELAY_MILLIS

    companion object {
        private const val DELAY_MILLIS = 200L

        private var previousClickTimeMillis = 0L
    }
}
