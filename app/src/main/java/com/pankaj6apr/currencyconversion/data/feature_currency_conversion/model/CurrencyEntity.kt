package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.Currency

@Entity
data class CurrencyEntity (
    @PrimaryKey val symbol: String,
    val name: String,
    val timeStamp: Long
)

fun CurrencyEntity.toCurrency() : Currency {
    return Currency(
        symbol = symbol,
        name = name,
        lastFetchTimeStamp = timeStamp
    )
}