package com.ryftpay.android.ui.service

import android.app.Activity
import com.ryftpay.android.core.model.payment.ChallengeAction
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.ThreeDsTransactionParams
import com.ryftpay.android.ui.model.threeds.ThreeDsChallengeResult

internal interface ThreeDsService {
    suspend fun createTransaction(identifyAction: IdentifyAction): ThreeDsTransactionParams
    suspend fun doChallenge(activity: Activity, challengeAction: ChallengeAction): ThreeDsChallengeResult
    fun cleanup()
}
