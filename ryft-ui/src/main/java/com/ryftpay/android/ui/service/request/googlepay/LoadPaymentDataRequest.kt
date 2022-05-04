package com.ryftpay.android.ui.service.request.googlepay

internal data class LoadPaymentDataRequest(
    val merchantInfo: MerchantInfo,
    val tokenizationSpecification: RyftTokenizationSpecification,
    val transactionInfo: TransactionInfo
)
