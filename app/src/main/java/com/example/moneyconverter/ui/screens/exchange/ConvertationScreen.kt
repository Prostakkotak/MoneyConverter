package com.example.moneyconverter.ui.screens.exchange

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertationScreen(
    exchangeViewModel: ExchangeViewModel,
    exchangeState: ExchangeState,
    navController: NavController
) {
    Scaffold(modifier = Modifier
        .padding(top = 64.dp, start = 16.dp, end = 16.dp)
        .fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Column() {
                if (exchangeState.fromCurrency != null) {
                    ConvertionCard(
                        modifier = Modifier.padding(bottom = 32.dp),
                        label = "Из:",
                        currencyCard = { CurrencyCard(
                            shortName = exchangeState.fromCurrency.shortName,
                            isFavorite = exchangeState.fromCurrency.isFavorite,
                            id = exchangeState.fromCurrency.id,
                            hideIconButton = true,
                            fullName = exchangeState.fromCurrency.fullName
                        ) },
                        inputValue = exchangeState.fromInput.toString(),
                        onValueChange = { exchangeViewModel.setFromInput(it) }
                    )
                }

                if (exchangeState.toCurrency != null) {
                    ConvertionCard(
                        label = "В:",
                        currencyCard = { CurrencyCard(
                            shortName = exchangeState.toCurrency.shortName,
                            isFavorite = exchangeState.toCurrency.isFavorite,
                            id = exchangeState.toCurrency.id,
                            hideIconButton = true,
                            fullName = exchangeState.toCurrency.fullName,
                        ) },
                        inputValue = exchangeState.toInput,
                        onValueChange = { exchangeViewModel.setToInput(it) }
                    )
                }
            }

            Button(
                onClick = {
                    exchangeViewModel.convertCurrencies(
                        from = exchangeState.fromCurrency!!.id,
                        to = exchangeState.toCurrency!!.id,
                        value = exchangeState.fromInput.toDouble()
                    )
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 90.dp)
                    .height(64.dp),
                shape = RoundedCornerShape(20),
                enabled = exchangeState.fromInput.toDoubleOrNull() != null
            ) {
                Text(text = "Конвертировать")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertionCard(
    modifier: Modifier = Modifier,
    label: String,
    currencyCard: @Composable () -> Unit,
    inputValue: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label)
            currencyCard.invoke()
        }
        OutlinedTextField(
            value = inputValue,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20),
            maxLines = 1
        )
    }
}