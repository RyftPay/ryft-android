package com.ryftpay.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ryftpay.android.ui.model.GooglePayResult

internal class GooglePayResultViewModel : ViewModel() {

    private val googlePayResult: MutableLiveData<GooglePayResult> = MutableLiveData()

    internal fun getResult(): LiveData<GooglePayResult> = googlePayResult

    internal fun updateResult(googlePayResult: GooglePayResult) {
        this.googlePayResult.value = googlePayResult
    }
}
