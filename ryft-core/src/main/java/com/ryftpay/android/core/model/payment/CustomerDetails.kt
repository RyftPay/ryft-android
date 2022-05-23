package com.ryftpay.android.core.model.payment

data class CustomerDetails(
    val email: String
) {
    companion object {
        fun from(customerEmail: String?): CustomerDetails? = if (customerEmail == null) {
            null
        } else {
            CustomerDetails(
                email = customerEmail
            )
        }
    }
}
