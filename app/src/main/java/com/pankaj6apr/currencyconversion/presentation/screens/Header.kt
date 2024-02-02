package com.pankaj6apr.currencyconversion.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pankaj6apr.currencyconversion.presentation.feature_currency_conversion.LatestCurrencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(viewModel: LatestCurrencyViewModel) {

    var expanded by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = viewModel.amount.value,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) run {
                    viewModel.getConversionsFor(it)
                }
            },
            placeholder = { Text("Enter amount") }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 4.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${viewModel.baseCurrency.value.symbol} - ${viewModel.baseCurrency.value.name}")
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "More"
                    )
                }
            }

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (viewModel.state.value.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .width(500.dp)
                        .height(500.dp),
                    content = {
                        items(viewModel.currencies.value.size) { index ->
                            val currency = viewModel.currencies.value[index]
                            DropdownMenuItem(
                                text = { Text("${currency.symbol} - ${currency.name}") },
                                onClick = {
                                    viewModel.setBaseCurrency(viewModel.currencies.value[index])
                                    expanded = false
                                }
                            )
                        }
                    })
            }
        }
        CurrencyConversionListScreen(viewModel = viewModel)
    }
}