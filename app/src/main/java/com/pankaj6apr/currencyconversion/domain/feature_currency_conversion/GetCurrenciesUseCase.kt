package com.pankaj6apr.currencyconversion.domain.feature_currency_conversion

import com.pankaj6apr.currencyconversion.common.DatabaseException
import com.pankaj6apr.currencyconversion.common.Resource
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.Currency
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.repository.CurrencyConversionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrenciesUseCase @Inject constructor(
    private val repository: CurrencyConversionRepository
) {
    operator fun invoke(): Flow<Resource<List<Currency>>> = flow {
        emit(Resource.Loading())

        val localCurrencies = repository.getCurrenciesFromDatabase()
        emit(localCurrencies)

        var fetchFromServer = false

        localCurrencies.data?.let {
            if (localCurrencies.data.isEmpty() || localCurrencies.data[0].lastFetchTimeStamp == 0L) {
                emit(Resource.Loading())
                fetchFromServer = true
            }
        }
        if (fetchFromServer) {
            when (val remoteCurrencies = repository.getCurrenciesFromServer()) {
                is Resource.Success -> {
                    remoteCurrencies.data?.let {
                        try {
                            repository.updateCurrenciesInDatabase(it)
                        } catch (e: DatabaseException) {
                            emit(Resource.Error(e.message!!))
                        }
                    }
                    emit(repository.getCurrenciesFromDatabase())
                }

                else -> {
                    emit(remoteCurrencies)
                }
            }
        }
    }
}