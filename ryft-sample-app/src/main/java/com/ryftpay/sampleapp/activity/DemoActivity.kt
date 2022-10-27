package com.ryftpay.sampleapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.ryftpay.sampleapp.R
import com.ryftpay.sampleapp.extension.getParcelableArgs
import com.ryftpay.sampleapp.fragment.DemoFragment
import kotlinx.parcelize.Parcelize
import java.lang.IllegalArgumentException

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        val input = intent.getParcelableArgs(ARGUMENTS_INTENT_EXTRA, Arguments::class.java)
            ?: throw IllegalArgumentException("No arguments provided to activity")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container_demo,
                    DemoFragment.newInstance(input.publicApiKey)
                )
                .commitNow()
        }
    }

    @Parcelize
    private class Arguments(
        val publicApiKey: String
    ) : Parcelable

    companion object {
        private const val ARGUMENTS_INTENT_EXTRA = "com.ryftpay.sampleapp.activity.DemoActivity.Arguments"

        internal fun createIntent(
            context: Context,
            publicApiKey: String
        ): Intent = Intent(context, DemoActivity::class.java).apply {
            putExtra(
                ARGUMENTS_INTENT_EXTRA,
                Arguments(
                    publicApiKey
                )
            )
        }
    }
}
