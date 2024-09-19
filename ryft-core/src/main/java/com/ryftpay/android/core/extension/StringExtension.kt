package com.ryftpay.android.core.extension

fun String.extractPaymentSessionIdFromClientSecret(): String {
    val secretIndex = this.indexOf("_secret_")
    return if (secretIndex != -1) this.substring(0, secretIndex) else this
}
