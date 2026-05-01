package com.ryftpay.android.core.model.payment

import com.ryftpay.android.core.api.payment.RequiredActionResponse

data class RequiredAction(
    val type: RequiredActionType,
    val url: String?,
    val identify: IdentifyAction?,
    val challenge: ChallengeAction? = null
) {
    companion object {
        internal fun from(response: RequiredActionResponse): RequiredAction =
            RequiredAction(
                type = RequiredActionType.from(response.type),
                url = response.url,
                identify = response.identify?.let { IdentifyAction.from(it) },
                challenge = response.challenge?.let { ChallengeAction.from(it) }
            )
    }
}
