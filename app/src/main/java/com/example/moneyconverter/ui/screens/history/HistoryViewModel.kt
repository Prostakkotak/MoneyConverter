package com.example.moneyconverter.ui.screens.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyconverter.Graph
import com.example.moneyconverter.data.room.models.Currency
import com.example.moneyconverter.data.room.models.History
import com.example.moneyconverter.data.room.models.HistorySettings
import com.example.moneyconverter.ui.repository.Repository
import com.example.moneyconverter.ui.screens.exchange.ExchangeState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: Repository = Graph.repository
): ViewModel() {
    var uiState by mutableStateOf(HistoryState())
        private set
    var settingsState by mutableStateOf(SettingsState())

    init {
        getHistory()
        getSettings()
    }

    fun updateSettingsState(settings: HistorySettings) {
        settingsState = settingsState.copy(
            settings = settings
        )
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

    private fun createHistorySettings() {
        viewModelScope.launch {
            repository.insertHistorySettings(HistorySettings(
                id = "main"
            ))
        }

        getSettings()
    }

    fun updateHistorySettings(historySettings: HistorySettings) { // Функция НЕ ОБНОВЛЯЕТ БД, ТОЛЬКО UI STATE
        uiState = uiState.copy(settings = historySettings)
    }

    fun saveHistorySettings(historySettings: HistorySettings) {
        viewModelScope.launch {
            repository.updateHistorySettings(historySettings)
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            uiState = uiState.copy(
                settings = repository.getSingleHistorySettings().first()
            )
        }
    }

    fun getSettings() {
        viewModelScope.launch {
            repository.historySettings.collectLatest {
                if (it.size == 0) {
                    createHistorySettings()
                } else {
                    uiState = uiState.copy(
                        settings = it.first()
                    )
                    settingsState = settingsState.copy(
                        settings = it.first()
                    )
                }
            }
        }
    }

    fun getCurrencies() {
        viewModelScope.launch {
            repository.currency.collectLatest {
                settingsState = settingsState.copy(
                    currencies = it.map { currency -> currency.id }
                )
            }
        }
    }
}

data class HistoryState(
    val historyList: List<History> = emptyList(),
    val settings: HistorySettings? = null,
)

data class SettingsState(
    val settings: HistorySettings? = null,
    val currencies: List<String> = listOf()
)