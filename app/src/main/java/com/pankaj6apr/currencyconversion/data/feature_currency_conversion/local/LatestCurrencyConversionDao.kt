package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyConversionEntity

@Dao
interface LatestCurrencyConversionDao {
    @Query("SELECT * FROM CurrencyConversionEntity")
    suspend fun getLatestCurrencyConversions(): List<CurrencyConversionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyConversionData(entities: List<CurrencyConversionEntity>)

    @Query("DELETE FROM CurrencyConversionEntity WHERE symbol = :id")
    suspend fun deleteCurrencyConversionData(id: String)
}