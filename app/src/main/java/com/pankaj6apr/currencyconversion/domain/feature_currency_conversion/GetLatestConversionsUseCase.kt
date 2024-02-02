package com.pankaj6apr.currencyconversion.domain.feature_currency_conversion

import com.pankaj6apr.currencyconversion.common.DatabaseException
import com.pankaj6apr.currencyconversion.common.Resource
import com.pankaj6apr.currencyconversion.common.Utils
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.repository.CurrencyConversionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLatestConversionsUseCase @Inject constructor(
    private val repository: CurrencyConversionRepository
) {
    operator fun invoke(currency: String, amount: Double): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading())

        val localConversions = repository.getLatestConversionsFromDatabase()
        emit(Resource.Success(Utils.convertRates(currency, amount, localConversions.data)))

        var fetchFromServer = true
        localConversions.data?.let {
            if (localConversions.data.isEmpty() || localConversions.data[0].lastFetchTimeStamp == 0L) {
                emit(Resource.Loading())
            } else {
                if (!Utils.shouldFetchFromNetwork(localConversions.data[0].lastFetchTimeStamp)) {
                    fetchFromServer = false
                }
            }
        }
        if (fetchFromServer) {
            when (val remoteConversions = repository.getLatestConversionsFromServer()) {
                is Resource.Success -> {
                    remoteConversions.data?.let {
                        try {
                            repository.updateLatestConversionsInDatabase(it)
                        } catch (e: DatabaseException) {
                            emit(Resource.Error(e.message!!))
                        }
                    }
                    emit(Resource.Success(Utils.convertRates(
                        currency,
                        amount,
                        repository.getLatestConversionsFromDatabase().data)
                    ))
                }
                else -> {
                    emit(remoteConversions)
                }
            }
        }
    }
}