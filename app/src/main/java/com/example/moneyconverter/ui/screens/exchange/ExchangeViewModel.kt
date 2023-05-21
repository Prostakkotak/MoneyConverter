package com.example.moneyconverter.ui.screens.exchange

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyconverter.Graph
import com.example.moneyconverter.ui.repository.Repository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.moneyconverter.data.room.models.Currency

class ExchangeViewModel(
    private val repository: Repository = Graph.repository
):ViewModel() {
    var uiState by mutableStateOf(ExchangeState())
        private set

    init {
        getCurrencies()
    }

    private fun getCurrencies() {
        viewModelScope.launch {
            repository.currency.collectLatest {
                uiState = uiState.copy(
                    currencyList = it
                )
            }
        }
    }

    fun insertCurrency(currency: Currency) {
        viewModelScope.launch {
            repository.insertCurrency(currency)
        }

        getCurrencies()
    }
}

data class ExchangeState(
    val currencyList: List<Currency> = emptyList(),
)