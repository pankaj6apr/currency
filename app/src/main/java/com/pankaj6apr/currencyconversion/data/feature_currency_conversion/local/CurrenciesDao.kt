package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model.CurrencyEntity

@Dao
interface CurrenciesDao {
    @Query("SELECT * FROM CurrencyEntity")
    suspend fun getCurrencies(): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(entities: List<CurrencyEntity>)


    @Query("DELETE FROM CurrencyEntity WHERE symbol = :id")
    suspend fun deleteCurrency(id: String)
}