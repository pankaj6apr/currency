package com.pankaj6apr.currencyconversion.presentation.feature_currency_conversion

import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.CurrencyRate

data class LatestConversionsState (
    val conversions: List<CurrencyRate>,
    val error: String?,
    val isLoading: Boolean = false,
)