package com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model

data class Currency(
    val symbol: String = "",
    val name: String = "",
    val lastFetchTimeStamp: Long = 0
)
