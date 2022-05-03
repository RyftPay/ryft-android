package com.ryftpay.android.ui.extension

import java.math.BigDecimal
import java.util.Currency

internal fun Int.formatPriceWithoutCurrencySymbol(currency: Currency): String = this.toBigDecimal()
    .divide(BigDecimal.valueOf(currency.divisor()))
    .setScale(currency.defaultFractionDigits)
    .toString()
