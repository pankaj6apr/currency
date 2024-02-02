package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyConversionEntity
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyEntity

@Database(
    entities = [CurrencyEntity::class],
    version = 1
)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract val dao: CurrenciesDao

    companion object {
        const val DATABASE_NAME = "currencies_db"
    }
}