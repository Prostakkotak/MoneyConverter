package com.example.moneyconverter.ui.screens.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyconverter.Graph
import com.example.moneyconverter.data.room.models.Currency
import com.example.moneyconverter.data.room.models.History
import com.example.moneyconverter.ui.repository.Repository
import com.example.moneyconverter.ui.screens.exchange.ExchangeState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: Repository = Graph.repository
): ViewModel() {
    var uiState by mutableStateOf(HistoryState())
        private set

    init {
        getHistory()
    }

    private fun getHistory() {
        viewModelScope.launch {
            repository.history.collectLatest {
                uiState = uiState.copy(
                    historyList = it
                )
            }
        }
    }

    fun insertCurrency(history: History) {
        viewModelScope.launch {
            repository.insertHistory(history)
        }

        getHistory()
    }
}

data class HistoryState(
    val historyList: List<History> = emptyList(),
)