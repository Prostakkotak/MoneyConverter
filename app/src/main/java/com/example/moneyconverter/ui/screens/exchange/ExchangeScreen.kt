package com.example.moneyconverter.ui.screens.exchange

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeBaseScreen() {
    val exchangeViewModel: ExchangeViewModel = viewModel(modelClass = ExchangeViewModel::class.java)
    val exchangeState: ExchangeState = exchangeViewModel.uiState

    val exchangeNavController = rememberNavController()
    
    Scaffold(
        topBar = { TopBar(navController = exchangeNavController, exchangeState = exchangeState) }
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
    val sortedCurrencies: List<Currency> = exchangeState.currencyList.filter {
        if (exchangeState.searchInput == "") true else it.shortName.toLowerCase().contains(exchangeState.searchInput.toLowerCase())
    }.sortedWith(compareBy({it.isFavorite}, {it.lastTimeUsed})).reversed()
    
    Scaffold(modifier = Modifier.padding(top = 64.dp), topBar = {
        Box {
            Box(modifier = Modifier
                .matchParentSize()
                .background(Color.White))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = exchangeState.searchInput,
                    maxLines = 1,
                    label = { Text("Поиск") },
                    onValueChange = {
                                    exchangeViewModel.setSearchInput(it)
                    },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(50)
                )
                if (exchangeState.choosedToConvert != null) {
                    Text(text = "Конвертация из:", modifier = Modifier.padding())
                    CurrencyCard(
                        exchangeState.choosedToConvert.shortName,
                        exchangeState.choosedToConvert.isFavorite,
                        exchangeState.choosedToConvert.id,
                        onCardLongClick = {exchangeViewModel.setChoosedToConvert(null)},
                        hideIconButton = true
                    )
                    Divider()
                }
            }
        }
    }) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = if (exchangeState.choosedToConvert != null) 180.dp else 80.dp
            )) {

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
                            if (exchangeState.choosedToConvert == null) {
                                exchangeViewModel.setFromCurrency(currencyData.id)

                                var toCurrencyId: String? = null

                                sortedCurrencies.reversed().forEach { currency ->
                                    if (currency.id != currencyData.id && currency.isFavorite) {
                                        toCurrencyId = currency.id
                                        return@forEach
                                    }
                                }

                                if (toCurrencyId == null) {
                                    if (currencyData.id != "RUB") {
                                        toCurrencyId = "RUB"
                                    } else {
                                        toCurrencyId = "USD"
                                    }
                                }

                                exchangeViewModel.setToCurrency(toCurrencyId!!)
                            } else {
                                exchangeViewModel.setToCurrency(currencyData.id)
                            }

                            navController.navigate(TopNavBar.ConvertationPage.route)
                        }
                    )
                }
            }
        }
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