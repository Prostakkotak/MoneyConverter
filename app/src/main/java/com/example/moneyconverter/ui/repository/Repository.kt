package com.example.moneyconverter.ui.repository

import com.example.moneyconverter.data.room.CurrencyDao
import com.example.moneyconverter.data.room.ExchangeRateDao
import com.example.moneyconverter.data.room.HistoryDao
import com.example.moneyconverter.data.room.models.Currency
import com.example.moneyconverter.data.room.models.ExchangeRate
import com.example.moneyconverter.data.room.models.History

class Repository (
    private val currencyDao: CurrencyDao,
    private val historyDao: HistoryDao,
    private val exchangeRateDao: ExchangeRateDao
) {
    val currency = currencyDao.getAllCurrencies()
    val history = historyDao.getHistory()
    val exchangeRates = exchangeRateDao.getExchangeRates()

    fun getSingleCurrency(currencyId: String) = currencyDao.getSingleCurrency(currencyId)
    fun getSingleHistory(historyId: Int) = historyDao.getSingleHistory(historyId)
    fun getSingleExchangeRate(exchangeRateId: String) = exchangeRateDao.getSingleExchangeRate(exchangeRateId)

    suspend fun insertCurrency(currency: Currency) {
        currencyDao.insert(currency)
    }

    suspend fun insertExchangeRate(exchangeRate: ExchangeRate) {
        exchangeRateDao.insert(exchangeRate)
    }

    suspend fun updateExchangeRate(exchangeRate: ExchangeRate) {
        exchangeRateDao.update(exchangeRate)
    }

    suspend fun updateCurrency(currency: Currency) {
        currencyDao.update(currency)
    }

    suspend fun insertHistory(history: History) {
        historyDao.insert(history)
    }
}