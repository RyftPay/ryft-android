package com.ryftpay.android.ui.service

import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.ui.model.threeds.ThreeDsTransactionParams

internal interface ThreeDsService {
    suspend fun createTransaction(identifyAction: IdentifyAction): ThreeDsTransactionParams
    fun cleanup()
}
