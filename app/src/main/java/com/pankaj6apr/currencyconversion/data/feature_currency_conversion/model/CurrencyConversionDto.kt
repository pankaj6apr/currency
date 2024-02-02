package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model

import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate

data class CurrencyConversionDto(
    val base: String,
    val disclaimer: String,
    val license: String,
    val rates: Map<String, Double>,
    val timestamp: Int
)

fun CurrencyConversionDto.toCurrencyRates() : List<CurrencyRate> {
    return rates.map {
        CurrencyRate(it.key, it.value)
    }
}