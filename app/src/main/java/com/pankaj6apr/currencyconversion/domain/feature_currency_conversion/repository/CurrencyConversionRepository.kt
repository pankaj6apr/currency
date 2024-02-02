package com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.repository

import com.pankaj6apr.currencyconversion.common.Resource
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.Currency
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate

interface CurrencyConversionRepository {
    suspend fun getLatestConversionsFromServer(): Resource<List<CurrencyRate>>

    suspend fun getCurrenciesFromServer(): Resource<List<Currency>>

    suspend fun getLatestConversionsFromDatabase(): Resource<List<CurrencyRate>>

    suspend fun getCurrenciesFromDatabase(): Resource<List<Currency>>

    suspend fun updateLatestConversionsInDatabase(currencyRates :List<CurrencyRate>)

    suspend fun updateCurrenciesInDatabase(currencies :List<Currency>)

}