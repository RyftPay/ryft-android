package com.ryftpay.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ryftpay.android.ui.dropin.RyftPaymentResult

internal class RyftPaymentResultViewModel : ViewModel() {

    private val paymentResult: MutableLiveData<RyftPaymentResult> = MutableLiveData()

    internal fun getResult(): LiveData<RyftPaymentResult> = paymentResult

    internal fun updateResult(paymentResult: RyftPaymentResult) {
        this.paymentResult.value = paymentResult
    }
}
