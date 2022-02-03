package com.ryftpay.android.ui.extension

import android.text.InputFilter
import android.widget.TextView

// Add the given filter to the filters if it doesn't exist, else replace it
internal fun TextView.addOrReplaceFilter(filter: InputFilter) {
    val existingFiltersAsList = filters?.toMutableList()
    existingFiltersAsList?.removeAll { filter.javaClass == it.javaClass }
    existingFiltersAsList?.add(filter)
    filters = existingFiltersAsList?.toTypedArray()
}
