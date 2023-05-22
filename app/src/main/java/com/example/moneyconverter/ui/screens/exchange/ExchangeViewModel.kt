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
import kotlinx.coroutines.flow.first

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

    fun setToInput(num: String) {
        uiState = uiState.copy(
            toInput = num.toInt()
        )
    }

    fun setFromInput(num: String) {
        uiState = uiState.copy(
            fromInput = num.toInt()
        )
    }

    fun toggleFavoriteCurrency(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            val currencyData: Currency = repository.getSingleCurrency(id).first()

            repository.updateCurrency(Currency(
                id = id,
                isFavorite = isFavorite,
                shortName = currencyData.shortName,
                fullName = currencyData.fullName
            ))
        }
    }

    fun setFromCurrency(id: String) {
        viewModelScope.launch {
            val currency: Currency = repository.getSingleCurrency(id).first()

            uiState = uiState.copy(
                fromCurrency = currency
            )
        }
    }

    fun setChoosedToConvert(id: String?) {
        viewModelScope.launch {
            val currency: Currency? = if (id != null) repository.getSingleCurrency(id).first() else null

            uiState = uiState.copy(
                choosedToConvert = currency,
                toCurrency = currency
            )
        }
    }
}

data class ExchangeState(
    val currencyList: List<Currency> = emptyList(),
    val choosedToConvert: Currency? = null,

    val fromCurrency: Currency? = null,
    val toCurrency: Currency? = null,

    val fromInput: Int = 1,
    val toInput: Int = 1
)