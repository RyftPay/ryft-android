package com.ryftpay.android.ui.client

import android.content.Context
import com.ravelin.core.configparameters.ConfigParametersBuilder
import com.ravelin.threeDS2Service.instantiation.ThreeDS2ServiceInstance
import com.ryftpay.android.core.model.api.RyftEnvironment
import com.ryftpay.android.ui.extension.toRavelinEnvironment
import com.ryftpay.android.ui.service.DefaultRavelinThreeDsService
import kotlinx.coroutines.CoroutineScope

internal object RavelinThreeDsServiceFactory {

    private const val SANDBOX_API_TOKEN = "Bearer publishable_key_test_Vr7SqPZFjLgqtnnS4bfPCwgRzIZCsgDrvyJRAx"
    private const val PRODUCTION_API_TOKEN = "pk_live_ravelin_placeholder"

    suspend fun create(
        context: Context,
        ryftEnvironment: RyftEnvironment,
        coroutineScope: CoroutineScope
    ): DefaultRavelinThreeDsService {
        val apiToken = when (ryftEnvironment) {
            RyftEnvironment.Prod -> PRODUCTION_API_TOKEN
            else -> SANDBOX_API_TOKEN
        }
        val config = ConfigParametersBuilder.Builder()
            .setApiToken(apiToken)
            .setEnvironment("EuLive")
            .build()
        val service = ThreeDS2ServiceInstance.get()
        service.initialize(context, config, null, null, coroutineScope)
        return DefaultRavelinThreeDsService(context, service, ryftEnvironment)
    }
}
