package com.pankaj6apr.currencyconversion.di

import android.app.Application
import androidx.room.Room
import com.pankaj6apr.currencyconversion.common.Constants
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local.CurrenciesDatabase
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local.CurrencyConversionDatabase
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.remote.CurrencyConversionAPI
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.repository.CurrencyConversionRepositoryImpl
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.repository.CurrencyConversionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCurrencyConversionApi(): CurrencyConversionAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyConversionAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyConversionDatabase(app: Application): CurrencyConversionDatabase {
        return Room.databaseBuilder(
            app,
            CurrencyConversionDatabase::class.java,
            CurrencyConversionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrenciesDatabase(app: Application): CurrenciesDatabase {
        return Room.databaseBuilder(
            app,
            CurrenciesDatabase::class.java,
            CurrenciesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyConversionRepository(api: CurrencyConversionAPI,
                                            conversionDb: CurrencyConversionDatabase,
                                            currenciesDb: CurrenciesDatabase
    ): CurrencyConversionRepository {
        return CurrencyConversionRepositoryImpl(api, conversionDb.dao, currenciesDb.dao)
    }

}