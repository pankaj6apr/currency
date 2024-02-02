package com.pankaj6apr.currencyconversion.domain.feature_currency_conversion

import com.google.common.truth.Truth.assertThat
import com.pankaj6apr.currencyconversion.common.Resource
import com.pankaj6apr.currencyconversion.data.repository.MockClock
import com.pankaj6apr.currencyconversion.data.repository.MockCurrencyConversionsRepository
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.repository.CurrencyConversionRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.math.abs

class GetLatestConversionsUnitTest {
    private lateinit var repository: CurrencyConversionRepository
    private lateinit var useCase: GetLatestConversionsUseCase

    @Before
    fun setUp() {
        repository = MockCurrencyConversionsRepository()
        useCase = GetLatestConversionsUseCase(repository)
    }

    @Test
    fun testRemoteAndLocalDataIsSame() = runTest {
        val baseCurrency = "AED"
        val amount = 1.0
        val remoteData = repository.getLatestConversionsFromServer().data
        var localData = listOf<CurrencyRate>()

        useCase(baseCurrency, amount).collectLatest {
            if (it is Resource.Success) {
                localData = repository.getLatestConversionsFromDatabase().data!!
            }
        }
        assertThat(localData.size).isEqualTo(remoteData!!.size)
        localData.forEach { localCurrencyRate ->
            val remoteCurrencyRate = remoteData.find {
                it.currency == localCurrencyRate.currency
            }
            assertThat(localCurrencyRate.currency).isEqualTo(remoteCurrencyRate?.currency)
            assertThat(localCurrencyRate.rate).isEqualTo(remoteCurrencyRate?.rate)
        }
    }

    @Test
    fun testCurrencyConversionLogic() = runTest {
        val amountMultiplier = 3
        val baseCurrency = "AED"
        val amount = amountMultiplier * 1.0
        var conversionRatio: Double

        val remoteData = repository.getLatestConversionsFromServer().data
        val USDCurrencyRate = remoteData?.find {
            it.currency == baseCurrency
        }
        conversionRatio = amountMultiplier / USDCurrencyRate?.rate!!

        // run usecase to convert 3 AED to all currencies
        var convertedData = listOf<CurrencyRate>()
        useCase(baseCurrency, amount).collectLatest {
            if (it is Resource.Success) {
                convertedData = it.data!!
            }
        }

        assertThat(convertedData.size).isEqualTo(remoteData!!.size)

        // check if the converted data by usecase actually follows the conversion logic
        convertedData.forEach { convertedCurrencyRate ->
            val remoteCurrencyRate = remoteData.find {
                it.currency == convertedCurrencyRate.currency
            }
            assertThat(remoteCurrencyRate).isNotNull()
            assertThat(
                abs(convertedCurrencyRate.rate - conversionRatio * remoteCurrencyRate!!.rate)
            ).isLessThan(0.00001)
        }
    }

    @Test
    fun testLocalDatabase() = runTest {
        val baseCurrency = "AED"
        val amount = 1.0
        var localData = repository.getLatestConversionsFromDatabase().data
        // local data is initially empty
        assertThat(localData).isEmpty()

        useCase(baseCurrency, amount).collectLatest {}

        localData = repository.getLatestConversionsFromDatabase().data

        // local data should not be empty after usecase runs
        assertThat(localData).isNotEmpty()
    }

    @Test
    fun testServerCallOnlyAllowedAfter30Minutes() = runTest {
        val baseCurrency = "AED"
        val amount = 1.0

        // would be 1 local call is made, 2 if server call is also made
        var numRepositoryCall = 0
        // First usecase execution
        useCase(baseCurrency, amount).collectLatest {
            if (it is Resource.Success) {
                numRepositoryCall++
            }
        }
        // both server and local calls are made
        assertThat(numRepositoryCall).isEqualTo(2)

        numRepositoryCall = 0
        // Second usecase execution
        useCase(baseCurrency, amount).collectLatest {
            if (it is Resource.Success) {
                numRepositoryCall++
            }
        }
        // only local call is made the second time
        assertThat(numRepositoryCall).isEqualTo(1)

        // simulate 30 minutes passed by changing timestamp in database
        val delayMillis: Long = 30 * 60 * 1000 + 1
        MockClock.mockDelay(-delayMillis)
        var localData = repository.getLatestConversionsFromDatabase().data!!
        repository.updateLatestConversionsInDatabase(localData)

        numRepositoryCall = 0
        // usecase execution after 30 minutes
        useCase(baseCurrency, amount).collectLatest {
            if (it is Resource.Success) {
                numRepositoryCall++
            }
        }
        // both local and server calls are made after 30 minutes
        assertThat(numRepositoryCall).isEqualTo(2)
    }
}