package com.ryftpay.android.core.model.error

import com.ryftpay.android.core.api.error.RyftErrorElementResponse

data class RyftErrorElement(
    val code: String,
    val message: String
) {
    companion object {
        internal fun from(response: RyftErrorElementResponse): RyftErrorElement =
            RyftErrorElement(
                code = response.code,
                message = response.message
            )
    }
}
