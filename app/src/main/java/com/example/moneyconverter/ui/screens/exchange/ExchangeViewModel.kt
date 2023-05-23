package com.example.moneyconverter.ui.screens.exchange

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.moneyconverter.data.room.models.ExchangeRate
import com.example.moneyconverter.data.room.models.History
import com.example.moneyconverter.network.API
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class ExchangeViewModel(
    private val repository: Repository = Graph.repository
):ViewModel() {
    var uiState by mutableStateOf(ExchangeState())
        private set

    private val AppApi: API = API()

    init {
        getCurrencies()
    }

    private fun updateExchangeRates() {
        viewModelScope.launch {
            val exchangeRates: JSONObject = AppApi.getExchangeRates()

            exchangeRates.keys().forEach { key ->
                val exchangeRate = exchangeRates.getJSONObject(key)

                insertExchangeRate(ExchangeRate(
                    id = key,
                    value = exchangeRate.getDouble("value")
                ))
            }
        }
    }

    private fun fillCurrencyDB() { // Заполняем БД запрашивая с API список валют
        viewModelScope.launch {
            val currencies: JSONObject = AppApi.getCurrencies()

            currencies.keys().forEach {key ->
                val currency = currencies.getJSONObject(key)

                insertCurrency(Currency(
                    id = currency.getString("code"),
                    shortName = currency.getString("code"),
                    fullName = currency.getString("name"),
                    isFavorite = false,
                    lastTimeUsed = Date()
                ))
            }
        }
    }

    private fun getCurrencies() {
        viewModelScope.launch {
            repository.currency.collectLatest {
                if (it.isEmpty()) { // Если БД пустая, идём её наполнять
                    fillCurrencyDB()
                    updateExchangeRates()
                } else {
                    uiState = uiState.copy(
                        currencyList = it
                    )
                }
            }
        }
    }

    private fun insertCurrency(currency: Currency) {
        viewModelScope.launch {
            repository.insertCurrency(currency)
        }
    }

    private fun insertExchangeRate(exchangeRate: ExchangeRate) {
        viewModelScope.launch {
            repository.insertExchangeRate(exchangeRate)
        }
    }

    private fun insertHistory(history: History) {
        viewModelScope.launch {
            repository.insertHistory(history)
        }
    }

    fun setToInput(num: String) {
        uiState = uiState.copy(
            toInput = num
        )
    }

    fun setFromInput(num: String) {
        uiState = uiState.copy(
            fromInput = num
        )
    }

    fun toggleFavoriteCurrency(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            val currencyData: Currency = repository.getSingleCurrency(id).first()

            repository.updateCurrency(currencyData.copy(
                isFavorite = !currencyData.isFavorite
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

    fun setToCurrency(id: String) {
        viewModelScope.launch {
            val currency: Currency = repository.getSingleCurrency(id).first()

            uiState = uiState.copy(
                toCurrency = currency
            )
        }
    }

    fun setChoosedToConvert(id: String?) {
        viewModelScope.launch {
            val currency: Currency? = if (id != null) repository.getSingleCurrency(id).first() else null

            uiState = uiState.copy(
                choosedToConvert = currency,
                fromCurrency = currency
            )
        }
    }

    fun setSearchInput(text: String) {
        uiState = uiState.copy(
            searchInput = text
        )
    }

    fun convertCurrencies(from: String, to: String, value: Double) {
        viewModelScope.launch {
            val fromRate = repository.getSingleExchangeRate(from).first().value
            val toRate = repository.getSingleExchangeRate(to).first().value

            val result: Double = Math.round((value / fromRate * toRate) * 1000.0) / 1000.0 // Сокращаем до 3 после запятой

            if (uiState.toCurrency!!.id == to) {
                uiState = uiState.copy(
                     toInput = result.toString()
                )
            } else {
                uiState = uiState.copy(
                    fromInput = result.toString()
                )
            }

            insertHistory( // Записываем в историю операцию
                History(
                    date = Date(),
                    fromValue = value,
                    toValue = result,
                    fromCurrency = from,
                    toCurrency = to,
                )
            )

            repository.updateCurrency( // Обновляем последнюю дату использования валютам
                repository.getSingleCurrency(from).first().copy(
                    lastTimeUsed = Date()
                )
            )
            repository.updateCurrency(
                repository.getSingleCurrency(to).first().copy(
                    lastTimeUsed = Date()
                )
            )
        }
    }
}

data class ExchangeState(
    val currencyList: List<Currency> = emptyList(),
    val choosedToConvert: Currency? = null,

    val fromCurrency: Currency? = null,
    val toCurrency: Currency? = null,

    val fromInput: String = "1",
    val toInput: String = "",

    val searchInput: String = ""
)