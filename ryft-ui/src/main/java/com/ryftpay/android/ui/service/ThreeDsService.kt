package com.ryftpay.android.ui.service

import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.ui.model.threeds.ThreeDsIdentificationResultListener

internal interface ThreeDsService {
    fun handleIdentification(
        identifyAction: IdentifyAction,
        listener: ThreeDsIdentificationResultListener
    )
}
