package com.ryftpay.android.core.model.error

import com.ryftpay.android.core.api.error.RyftErrorResponse
import java.util.UUID

data class RyftError(
    val requestId: String?,
    val httpStatusCode: String,
    val errors: List<RyftErrorElement>
) {
    companion object {
        val Unknown = RyftError(
            requestId = UUID.randomUUID().toString(),
            httpStatusCode = "500",
            errors = emptyList()
        )

        internal fun from(response: RyftErrorResponse): RyftError =
            RyftError(
                requestId = response.requestId,
                httpStatusCode = response.code,
                errors = response.errors.map { RyftErrorElement.from(it) },
            )
    }
}
