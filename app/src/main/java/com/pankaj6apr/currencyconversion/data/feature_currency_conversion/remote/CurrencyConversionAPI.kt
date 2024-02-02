package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.remote

import com.pankaj6apr.currencyconversion.common.Constants
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyConversionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConversionAPI {
    @GET("latest.json")
    suspend fun getLatestCurrencyConversion(
        @Query("app_id") apiKey: String = Constants.APP_ID
    ): Response<CurrencyConversionDto>

    @GET("currencies.json")
    suspend fun getCurrencies(
        @Query("app_id") apiKey: String = Constants.APP_ID
    ): Response<Map<String, String>>
}