package com.ryftpay.android.ui.model.googlepay

internal data class LoadPaymentDataRequest(
    val merchantInfo: MerchantInfo,
    val tokenizationSpecification: RyftTokenizationSpecification,
    val transactionInfo: TransactionInfo
)
