package com.ryftpay.android.ui.client

import android.content.Context
import com.ravelin.core.configparameters.ConfigParametersBuilder
import com.ravelin.threeDS2Service.instantiation.ThreeDS2ServiceInstance
import com.ryftpay.android.core.model.api.RyftEnvironment
import com.ryftpay.android.ui.extension.toRavelinEnvironment
import com.ryftpay.android.ui.service.DefaultRavelinThreeDsService
import kotlinx.coroutines.CoroutineScope

internal object RavelinThreeDsServiceFactory {

    private const val BEARER_PREFIX = "Bearer "

    suspend fun create(
        context: Context,
        ryftEnvironment: RyftEnvironment,
        ravelinPublicKey: String,
        coroutineScope: CoroutineScope
    ): DefaultRavelinThreeDsService {
        val key = ravelinPublicKey.trim()
        require(key.isNotEmpty()) { "Ravelin public key cannot be empty" }
        val config = ConfigParametersBuilder.Builder()
            .setApiToken(key.toApiToken())
            .setEnvironment("EuLive")
            .build()
        val service = ThreeDS2ServiceInstance.get()
        service.initialize(context, config, null, null, coroutineScope)
        return DefaultRavelinThreeDsService(context, service, ryftEnvironment)
    }

    private fun String.toApiToken(): String =
        if (startsWith(BEARER_PREFIX)) this else "$BEARER_PREFIX$this"
}
