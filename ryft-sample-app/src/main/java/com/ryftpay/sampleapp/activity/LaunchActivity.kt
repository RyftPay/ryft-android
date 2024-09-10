package com.ryftpay.sampleapp.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ryftpay.sampleapp.R

class LaunchActivity : AppCompatActivity() {

    private var launchDemoButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        launchDemoButton = findViewById(R.id.button_launch_demo)

        launchDemoButton?.setOnClickListener {
            launchDemo(publicApiKey = publicApiKeyInput?.text?.toString() ?: "")
        }
    }

    private fun launchDemo() =
        this.startActivity(DemoActivity.createIntent(this))
}
