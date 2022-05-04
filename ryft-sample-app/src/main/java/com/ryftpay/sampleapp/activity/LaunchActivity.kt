package com.ryftpay.sampleapp.activity

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ryftpay.sampleapp.R

class LaunchActivity : AppCompatActivity() {

    private var launchDemoButton: Button? = null
    private var publicApiKeyInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        launchDemoButton = findViewById(R.id.button_launch_demo)
        publicApiKeyInput = findViewById(R.id.input_public_api_key)

        launchDemoButton?.setOnClickListener {
            launchDemo(publicApiKey = publicApiKeyInput?.text?.toString() ?: "")
        }
    }

    private fun launchDemo(publicApiKey: String) = if (isPublicApiKeyValid(publicApiKey)) {
        this.startActivity(DemoActivity.createIntent(this, publicApiKey))
    } else {
        AlertDialog.Builder(this)
            .setTitle("Error launching demo")
            .setMessage("Invalid sandbox public api key")
            .setNegativeButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun isPublicApiKeyValid(publicApiKey: String): Boolean =
        publicApiKey.startsWith(VALID_PUBLIC_API_KEY_PREFIX)

    companion object {
        private const val VALID_PUBLIC_API_KEY_PREFIX = "pk_sandbox_"
    }
}
