package com.ryftpay.android.ui.dropin.threeds

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.ryftpay.android.core.model.api.RyftPublicApiKey
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class DefaultRyftRequiredActionComponentTest {

    private val fragment = mockk<Fragment>(relaxed = true)
    private val activity = mockk<ComponentActivity>(relaxed = true)
    private val listener = mockk<RyftRequiredActionResultListener>(relaxed = true)
    private val publicApiKey = RyftPublicApiKey("pk_sandbox_123")

    @Test
    internal fun `DefaultRyftRequiredActionComponent should register fragment for activity on construction with public api key`() {
        DefaultRyftRequiredActionComponent(
            fragment,
            listener,
            publicApiKey
        )

        verify {
            fragment.registerForActivityResult(
                match<ActivityResultContract<Intent, RyftRequiredActionResult>> {
                    it is RyftRequiredActionResultContract
                },
                any()
            )
        }
    }

    @Test
    internal fun `DefaultRyftRequiredActionComponent should register activity for activity on construction with public api key`() {
        DefaultRyftRequiredActionComponent(
            activity,
            listener,
            publicApiKey
        )

        verify {
            activity.registerForActivityResult(
                match<ActivityResultContract<Intent, RyftRequiredActionResult>> {
                    it is RyftRequiredActionResultContract
                },
                any()
            )
        }
    }

    @Test
    internal fun `DefaultRyftRequiredActionComponent should register fragment for activity on construction without public api key`() {
        DefaultRyftRequiredActionComponent(
            fragment,
            listener
        )

        verify {
            fragment.registerForActivityResult(
                match<ActivityResultContract<Intent, RyftRequiredActionResult>> {
                    it is RyftRequiredActionResultContract
                },
                any()
            )
        }
    }

    @Test
    internal fun `DefaultRyftRequiredActionComponent should register activity for activity on construction without public api key`() {
        DefaultRyftRequiredActionComponent(
            activity,
            listener
        )

        verify {
            activity.registerForActivityResult(
                match<ActivityResultContract<Intent, RyftRequiredActionResult>> {
                    it is RyftRequiredActionResultContract
                },
                any()
            )
        }
    }
}
