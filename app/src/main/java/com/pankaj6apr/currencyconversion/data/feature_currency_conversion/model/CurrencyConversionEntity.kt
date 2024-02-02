package com.pankaj6apr.currencyconversion.data.feature_currency_conversion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate

@Entity
data class CurrencyConversionEntity (
    @PrimaryKey val symbol: String,
    val amount: Double,
    val timeStamp: Long
)

fun CurrencyConversionEntity.toCurrencyRate(): CurrencyRate {
    return CurrencyRate(
        currency = symbol,
        rate = amount,
        lastFetchTimeStamp = timeStamp
    )
}