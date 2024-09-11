package com.ryftpay.sampleapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ryftpay.sampleapp.R
import com.ryftpay.sampleapp.fragment.DemoFragment

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container_demo,
                    DemoFragment.newInstance()
                )
                .commitNow()
        }
    }

    companion object {
        internal fun createIntent(
            context: Context
        ): Intent = Intent(context, DemoActivity::class.java)
    }
}
