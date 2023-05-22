package com.example.moneyconverter.ui.screens.exchange

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.moneyconverter.data.room.models.Currency

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeBaseScreen() {
    val exchangeViewModel: ExchangeViewModel = viewModel(modelClass = ExchangeViewModel::class.java)
    val exchangeState: ExchangeState = exchangeViewModel.uiState

    val exchangeNavController = rememberNavController()
    
    Scaffold(
        topBar = { TopBar(navController = exchangeNavController) }
    ) {
        TopNavGraph(navController = exchangeNavController, exchangeState, exchangeViewModel)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    exchangeViewModel: ExchangeViewModel,
    exchangeState: ExchangeState,
    navController: NavController
) {
    val sortedCurrencies: List<Currency> = exchangeState.currencyList.sortedByDescending { it.isFavorite }
    
    Scaffold(modifier = Modifier.padding(top = 64.dp)) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)) {

            if (exchangeState.choosedToConvert != null) {
                item {
                    Text(text = "Конвертация в:", modifier = Modifier.padding())
                    CurrencyCard(
                        exchangeState.choosedToConvert.shortName,
                        exchangeState.choosedToConvert.isFavorite,
                        exchangeState.choosedToConvert.id,
                        onIconClick = { exchangeViewModel.toggleFavoriteCurrency(exchangeState.choosedToConvert.id, !exchangeState.choosedToConvert.isFavorite) },
                        onCardLongClick = {exchangeViewModel.setChoosedToConvert(null)}
                    )
                    Divider()
                }
            }

            items(sortedCurrencies.size) {index: Int ->
                val currencyData = sortedCurrencies[index]

                if (exchangeState.choosedToConvert?.id != currencyData.id) {
                    CurrencyCard(
                        shortName = currencyData.shortName,
                        isFavorite = currencyData.isFavorite,
                        id = currencyData.id,
                        onIconClick = {
                            exchangeViewModel.toggleFavoriteCurrency(
                                currencyData.id,
                                !currencyData.isFavorite
                            )
                        },
                        onCardLongClick = {
                            exchangeViewModel.setChoosedToConvert(currencyData.id)
                        },
                        onCardClick = {
                            exchangeViewModel.setFromCurrency(currencyData.id)
                            navController.navigate(TopNavBar.ConvertationPage.route)
                        }
                    )
                }
            }
        }
    }
}

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
                            fullName = exchangeState.toCurrency.fullName
                        ) },
                        inputValue = exchangeState.toInput.toString(),
                        onValueChange = { exchangeViewModel.setToInput(it) }
                    )
                }
            }
            
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 90.dp)
                    .height(64.dp),
                shape = RoundedCornerShape(20)
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
            shape = RoundedCornerShape(20)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable // Карточка валюты
fun CurrencyCard(
    shortName: String,
    isFavorite: Boolean,
    id: String,
    onIconClick: () -> Unit = {},
    onCardLongClick: () -> Unit = {},
    onCardClick: () -> Unit = {},
    hideIconButton: Boolean = false,
    fullName: String? = null
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, bottom = 8.dp)
        .combinedClickable(
            onLongClick = onCardLongClick,
            onClick = onCardClick
        )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(shortName, fontSize = 24.sp, modifier = Modifier.padding(start = 16.dp))

            if (!hideIconButton) {
                IconButton(
                    onClick = onIconClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        tint = if (isFavorite) Color.Yellow else Color.Gray,
                        contentDescription = "",
                    )
                }
            }
        }

        if (fullName != null) {
            Text(text = fullName, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        }
    }
}