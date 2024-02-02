package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyConversionEntity

@Database(
    entities = [CurrencyConversionEntity::class],
    version = 1
)
abstract class CurrencyConversionDatabase : RoomDatabase() {
    abstract val dao: LatestCurrencyConversionDao

    companion object {
        const val DATABASE_NAME = "currency_conversion_db"
    }
}