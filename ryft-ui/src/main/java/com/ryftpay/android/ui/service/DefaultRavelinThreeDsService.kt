package com.ryftpay.android.ui.service

import android.content.Context
import com.ul.emvco3ds.sdk.spec.ThreeDS2Service
import com.ul.emvco3ds.sdk.spec.Transaction
import com.ryftpay.android.core.model.payment.IdentifyAction
import com.ryftpay.android.core.model.payment.ThreeDsTransactionParams
import com.ryftpay.android.ui.extension.toDirectoryServerId

internal class DefaultRavelinThreeDsService(
    private val context: Context,
    private val threeDS2Service: ThreeDS2Service
) : ThreeDsService {

    private var transaction: Transaction? = null

    override suspend fun createTransaction(identifyAction: IdentifyAction): ThreeDsTransactionParams {
        val tx = threeDS2Service.createTransaction(
            identifyAction.scheme.toDirectoryServerId(),
            identifyAction.protocolVersion  // Ravelin calls this messageVersion
        )
        transaction = tx
        val params = requireNotNull(tx.getAuthenticationRequestParameters()) {
            "Ravelin SDK returned null AuthenticationRequestParameters"
        }
        return ThreeDsTransactionParams(
            sdkTransactionId = params.getSDKTransactionID()!!,
            sdkApplicationId = params.getSDKAppID()!!,
            sdkEncryptedData = params.getDeviceData()!!,
            sdkEphemeralPublicKey = params.getSDKEphemeralPublicKey()!!,
            sdkReferenceNumber = params.getSDKReferenceNumber()!!
        )
    }

    override fun cleanup() {
        transaction?.close()
        threeDS2Service.cleanup(context)
    }
}
