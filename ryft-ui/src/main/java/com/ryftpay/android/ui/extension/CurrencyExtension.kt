package com.ryftpay.android.ui.extension

import java.util.Currency
import kotlin.math.pow

internal fun Currency.divisor(): Double = 10.0.pow(defaultFractionDigits)
