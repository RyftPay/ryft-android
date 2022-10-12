package com.ryftpay.android.ui.component

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.ryftpay.android.ui.extension.setOnSingleClickListener
import com.ryftpay.android.ui.listener.RyftCheckBoxListener
import com.ryftpay.ui.R

internal class RyftCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var checkBox: CheckBox
    private lateinit var label: TextView

    internal fun initialise(
        text: String,
        listener: RyftCheckBoxListener
    ) {
        checkBox = findViewById(R.id.chk_ryft_internal)
        checkBox.setOnCheckedChangeListener { _, checked ->
            checkBox.background = AppCompatResources.getDrawable(
                context,
                if (checked) {
                    R.drawable.layer_ryft_check_box_selected
                } else {
                    R.drawable.bg_ryft_check_box
                }
            )
            listener.onCheckBoxChanged(checked)
        }

        label = findViewById(R.id.text_ryft_check_box)
        label.text = text
        label.setOnSingleClickListener {
            checkBox.toggle()
        }
    }
}
