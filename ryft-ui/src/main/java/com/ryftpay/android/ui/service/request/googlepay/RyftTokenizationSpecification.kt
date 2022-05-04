package com.ryftpay.android.ui.service.request.googlepay

import com.ryftpay.android.core.model.api.RyftPublicApiKey

internal data class RyftTokenizationSpecification(
    val publicApiKey: RyftPublicApiKey
) {
    companion object {
        internal fun from(
            publicApiKey: RyftPublicApiKey
        ): RyftTokenizationSpecification = RyftTokenizationSpecification(
            publicApiKey
        )
    }
}
