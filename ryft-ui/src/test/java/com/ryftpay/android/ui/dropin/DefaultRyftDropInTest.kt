package com.ryftpay.android.ui.dropin

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class DefaultRyftDropInTest {

    private val fragment = mockk<Fragment>(relaxed = true)
    private val activity = mockk<ComponentActivity>(relaxed = true)
    private val listener = mockk<RyftDropInResultListener>(relaxed = true)
    private val publicApiKey = RyftPublicApiKey("pk_sandbox_123")

    @Test
    internal fun `DefaultRyftClient should register fragment for activity on construction`() {
        DefaultRyftDropIn(
            fragment,
            listener,
            publicApiKey
        )

        verify {
            fragment.registerForActivityResult(
                match<ActivityResultContract<Intent, RyftPaymentResult>> {
                    it is RyftDropInResultContract
                },
                any()
            )
        }
    }

    @Test
    internal fun `DefaultRyftClient should register activity for activity on construction`() {
        DefaultRyftDropIn(
            activity,
            listener,
            publicApiKey
        )

        verify {
            activity.registerForActivityResult(
                match<ActivityResultContract<Intent, RyftPaymentResult>> {
                    it is RyftDropInResultContract
                },
                any()
            )
        }
    }
}
