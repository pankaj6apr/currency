package com.pankaj6apr.currencyconversion.common

import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate

object Utils {
    // only make network call after 30 minutes
    fun shouldFetchFromNetwork(lastUpdateTime: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastUpdateTime
        return elapsedTime > 30 * 60 * 1000
    }

    fun convertRates(baseCurrency: String, amount: Double, list: List<CurrencyRate>?): List<CurrencyRate> {
        val thisCurrency = list?.find {
            it.currency == baseCurrency
        } ?: return listOf()

        var price = 0.0
        price = thisCurrency?.rate!!
        var res = list?.map {
            CurrencyRate(
                currency = it.currency,
                rate = amount * (it.rate/price)
            )
        }!!
        return res
    }
}