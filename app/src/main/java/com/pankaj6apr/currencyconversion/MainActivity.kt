package com.pankaj6apr.currencyconversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pankaj6apr.currencyconversion.presentation.feature_currency_conversion.LatestCurrencyViewModel
import com.pankaj6apr.currencyconversion.presentation.screens.Header
import com.pankaj6apr.currencyconversion.ui.theme.CurrencyConversionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConversionTheme {
                val viewModel: LatestCurrencyViewModel = hiltViewModel()
                val snackbarHostState = remember { SnackbarHostState() }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(key1 = true) {
                        viewModel.eventFlow.collectLatest { event ->
                            when (event) {
                                is LatestCurrencyViewModel.UIEvent.ShowSnackbar -> {
                                    snackbarHostState.showSnackbar(
                                        message = event.message
                                    )
                                }
                            }
                        }
                    }
                    Box {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                        Header(viewModel)
                    }
                }
            }
        }
    }
}
