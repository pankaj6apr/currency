package com.pankaj6apr.currencyconversion.presentation.feature_currency_conversion

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pankaj6apr.currencyconversion.common.Resource
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.GetCurrenciesUseCase
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.GetLatestConversionsUseCase
import com.pankaj6apr.currencyconversion.domain.feature_currency_conversion.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestCurrencyViewModel @Inject constructor(
    private val getLatestConversionsUseCase: GetLatestConversionsUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {
    private val _state =
        mutableStateOf(LatestConversionsState(conversions = listOf(), error = null))
    val state: MutableState<LatestConversionsState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _amount = mutableStateOf("0.0")
    val amount: State<String> = _amount

    private val _baseCurrency = mutableStateOf(Currency())
    val baseCurrency: State<Currency> = _baseCurrency

    private val _currencies = mutableStateOf<List<Currency>>(listOf())
    val currencies: State<List<Currency>> = _currencies

    init {
        viewModelScope.launch {
            getCurrenciesUseCase().collect {
                when (it) {
                    is Resource.Success -> {
                        _currencies.value = it.data ?: listOf()
                        if (!_currencies.value.isEmpty()) {
                            setBaseCurrency(_currencies.value[0])
                        }
                    }

                    is Resource.Loading -> {
                    }

                    is Resource.Error -> {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                it.message ?: "Unknown error"
                            )
                        )
                    }
                }
            }
        }
    }

    fun setBaseCurrency(currency: Currency) {
        _baseCurrency.value = currency
        getConversionsFor(amount.value)
    }

    fun getConversionsFor(amt: String) {
        _amount.value = amt
        if (amt.isNullOrEmpty()) {
            return
        }
        viewModelScope.launch {
            getLatestConversionsUseCase(
                baseCurrency.value.symbol,
                amount.value.toDouble()
            ).collect {
                when (it) {
                    is Resource.Loading -> {
                        state.value = LatestConversionsState(
                            conversions = listOf(),
                            error = null,
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        val conversions = it.data ?: listOf()
                        state.value = LatestConversionsState(
                            conversions = conversions,
                            error = null,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        state.value = LatestConversionsState(
                            conversions = state.value.conversions,
                            error = it.message,
                            isLoading = false
                        )

                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                it.message ?: "Unknown error"
                            )
                        )
                    }

                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}