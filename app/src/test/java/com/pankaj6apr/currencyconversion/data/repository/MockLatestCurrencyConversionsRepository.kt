package com.pankaj6apr.currencyconversion.data.repository

import com.pankaj6apr.currencyconversion.common.Resource
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.Currency
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.repository.CurrencyConversionRepository

class MockCurrencyConversionsRepository : CurrencyConversionRepository {
    var localData = listOf<CurrencyRate>()

    override suspend fun getLatestConversionsFromServer(): Resource<List<CurrencyRate>> {
        var serverData = mutableListOf<CurrencyRate>()
        serverData += CurrencyRate("AED", 3.673)
        serverData += CurrencyRate("INR", 80.0)
        serverData += CurrencyRate("USD", 1.0)
        serverData += CurrencyRate("AFN", 73.500003)
        serverData += CurrencyRate("ALL", 95.65)
        serverData += CurrencyRate("AMD", 404.753712)
        serverData += CurrencyRate("ANG", 1.799043)
        serverData += CurrencyRate("ARS", 826.8576)
        serverData += CurrencyRate("AUD", 1.517636)
        serverData += CurrencyRate("AWG", 1.8)
        serverData += CurrencyRate("AZN", 1.7)
        serverData += CurrencyRate("BAM", 1.801178)
        serverData += CurrencyRate("BDT", 109.554225)
        serverData += CurrencyRate("BGN", 1.79824)
        serverData += CurrencyRate("BHD", 0.376958)
        serverData += CurrencyRate("BND", 1.33784)
        serverData += CurrencyRate("BOB", 6.89779)
        serverData += CurrencyRate("BRL", 4.916599)
        serverData += CurrencyRate("BTC", 0.000023196692)
        serverData += CurrencyRate("BTN", 82.893931)
        serverData += CurrencyRate("BWP", 13.581128)
        serverData += CurrencyRate("BYN", 3.266212)
        serverData += CurrencyRate("BZD", 2.012046)
        serverData += CurrencyRate("CAD", 1.337441)
        serverData += CurrencyRate("CHF", 0.857435)
        serverData += CurrencyRate("CLF", 0.033779)
        serverData += CurrencyRate("CLP", 932.07)
        serverData += CurrencyRate("CNH", 7.188928)
        serverData += CurrencyRate("CNY", 7.101)
        serverData += CurrencyRate("COP", 3892.578086)
        serverData += CurrencyRate("CRC", 511.947153)
        serverData += CurrencyRate("CUP", 25.75)
        serverData += CurrencyRate("CVE", 102.1)
        serverData += CurrencyRate("CZK", 22.8656)
        serverData += CurrencyRate("DJF", 177.935943)
        serverData += CurrencyRate("DKK", 6.856738)
        serverData += CurrencyRate("DOP", 58.8)
        serverData += CurrencyRate("DZD", 134.659752)
        serverData += CurrencyRate("EGP", 30.9001)
        serverData += CurrencyRate("ETB", 56.45)
        serverData += CurrencyRate("EUR", 0.91981)
        serverData += CurrencyRate("GBP", 0.784559)
        serverData += CurrencyRate("IDR", 15744.631623)
        return Resource.Success(serverData)
    }

    override suspend fun getCurrenciesFromServer(): Resource<List<Currency>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestConversionsFromDatabase(): Resource<List<CurrencyRate>> {
        return Resource.Success(localData)
    }

    override suspend fun getCurrenciesFromDatabase(): Resource<List<Currency>> {
        return Resource.Success(listOf())
    }

    override suspend fun updateLatestConversionsInDatabase(currencyRates: List<CurrencyRate>) {
        val currentTime = MockClock.currentTimeMillis()
        localData = currencyRates.map {
            CurrencyRate(it.currency, it.rate, currentTime)
        }
    }

    override suspend fun updateCurrenciesInDatabase(currencies: List<Currency>) {
    }
}