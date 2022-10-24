package com.ryftpay.android.ui.client

import android.content.Context
import android.net.Uri
import com.checkout.threeds.Checkout3DSService
import com.checkout.threeds.domain.model.FooterCustomization
import com.checkout.threeds.domain.model.TextStyleCustomization
import com.checkout.threeds.domain.model.UICustomization
import com.ryftpay.android.core.model.api.RyftEnvironment
import com.ryftpay.android.ui.extension.toCheckoutComEnvironment
import com.ryftpay.ui.R
import java.util.Locale

internal object Checkout3dsServiceFactory {
    fun create(
        context: Context,
        ryftEnvironment: RyftEnvironment,
        returnUrl: String
    ): Checkout3DSService = Checkout3DSService(
        context,
        ryftEnvironment.toCheckoutComEnvironment(),
        Locale.getDefault(),
        uiCustomization = UICustomization(
            // Hides the footer
            footerCustomization = FooterCustomization(
                expandIndicatorColor = R.color.ryftThreeDsFormFooter,
                labelStyleCustomization = TextStyleCustomization(
                    textColor = R.color.ryftThreeDsFormFooter
                ),
                textStyleCustomization = TextStyleCustomization(
                    textColor = R.color.ryftThreeDsFormFooter
                )
            )
        ),
        appUrl = Uri.parse(returnUrl)
    )
}
