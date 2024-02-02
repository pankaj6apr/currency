package com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model

data class CurrencyRate(
    val currency: String,
    val rate: Double,
    val lastFetchTimeStamp: Long = 0
)
