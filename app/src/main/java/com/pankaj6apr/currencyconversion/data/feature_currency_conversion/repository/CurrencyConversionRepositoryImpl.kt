package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.repository

import com.pankaj6apr.currencyconversion.common.DatabaseException
import com.pankaj6apr.currencyconversion.common.Resource
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local.CurrenciesDao
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local.LatestCurrencyConversionDao
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyConversionEntity
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyEntity
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.toCurrency
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.toCurrencyRate
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.toCurrencyRates
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.remote.CurrencyConversionAPI
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.Currency
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.repository.CurrencyConversionRepository
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CurrencyConversionRepositoryImpl @Inject constructor(
    private val api: CurrencyConversionAPI,
    private val currencyConversionDao: LatestCurrencyConversionDao,
    private val currenciesDao: CurrenciesDao
) : CurrencyConversionRepository {

    override suspend fun getLatestConversionsFromServer(): Resource<List<CurrencyRate>> {
        var result: Resource<List<CurrencyRate>>
        try {
            val response = api.getLatestCurrencyConversion()

            if (response.isSuccessful && response.body() != null) {
                result = Resource.Success(response.body()!!.toCurrencyRates())
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                result = Resource.Error(errorObj.getString("status_message"))
            } else {
                result = Resource.Error("Something went wrong")
            }
        } catch (e: HttpException) {
            result = Resource.Error(
                message = "Something went wrong! HTTP error: ${e.code()}"
            )
        } catch (e: IOException) {
            result = Resource.Error(
                message = "Couldn't reach server, check your internet connection."
            )
        }
        return result
    }

    override suspend fun getLatestConversionsFromDatabase(): Resource<List<CurrencyRate>> {
        var result: Resource<List<CurrencyRate>>

        try {
            val localConversions = currencyConversionDao.getLatestCurrencyConversions().map {
                it.toCurrencyRate()
            }
            result = Resource.Success(localConversions)
        } catch (e: IllegalStateException) {
            result = Resource.Error(
                message = e.message ?: "Error fetching data from database"
            )
        } catch (e: Exception) {
            result = Resource.Error(
                message = "Error fetching data from database"
            )
        }
        return result
    }


    override suspend fun getCurrenciesFromServer(): Resource<List<Currency>> {

        var result: Resource<List<Currency>>

        try {
            val response = api.getCurrencies()

            if (response.isSuccessful && response.body() != null) {
                var currencies = listOf<Currency>()
                response.body()!!.map {
                    currencies += Currency(it.key, it.value)
                }
                result = Resource.Success(currencies)
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                result = Resource.Error(errorObj.getString("status_message"))
            } else {
                result = Resource.Error("Something went wrong")
            }
        } catch (e: HttpException) {
            result =
                Resource.Error(
                    message = "Something went wrong! HTTP error: ${e.code()}"
                )

        } catch (e: IOException) {
            result =
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection."
                )
        }
        return result
    }

    override suspend fun getCurrenciesFromDatabase(): Resource<List<Currency>> {
        var result: Resource<List<Currency>>

        try {
            val localCurrencies = currenciesDao.getCurrencies().map {
                it.toCurrency()
            }
            result = Resource.Success(localCurrencies)
        } catch (e: IllegalStateException) {
            result = Resource.Error(
                message = e.message ?: "Error fetching data from database"
            )
        } catch (e: Exception) {
            result = Resource.Error(
                message = "Error fetching data from database"
            )
        }
        return result
    }

    override suspend fun updateLatestConversionsInDatabase(currencyRates: List<CurrencyRate>) {
        try {
            currencyRates.map {
                currencyConversionDao.deleteCurrencyConversionData(it.currency)
            }

            val currentTime = System.currentTimeMillis()
            currencyConversionDao.insertCurrencyConversionData(
                currencyRates.map {
                    CurrencyConversionEntity(
                        symbol = it.currency,
                        amount = it.rate,
                        timeStamp = currentTime
                    )
                }
            )
        } catch (e: Exception) {
            throw DatabaseException(e.message ?: "Error handling data from database")
        }
    }

    override suspend fun updateCurrenciesInDatabase(currencies: List<Currency>) {
        try {
            currencies.map {
                currenciesDao.deleteCurrency(it.symbol)
            }

            val currentTime = System.currentTimeMillis()
            currenciesDao.insertCurrencies(
                currencies.map {
                    CurrencyEntity(
                        symbol = it.symbol,
                        name = it.name,
                        timeStamp = currentTime
                    )
                }
            )
        } catch (e: Exception) {
            throw DatabaseException(e.message ?: "Error handling data from database")
        }
    }
}