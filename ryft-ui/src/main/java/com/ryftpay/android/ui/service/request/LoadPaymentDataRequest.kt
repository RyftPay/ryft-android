package com.ryftpay.android.ui.service.request

internal data class LoadPaymentDataRequest(
    val merchantInfo: MerchantInfo,
    val tokenizationSpecification: RyftTokenizationSpecification,
    val transactionInfo: TransactionInfo
)
