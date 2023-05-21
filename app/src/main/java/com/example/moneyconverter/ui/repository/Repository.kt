package com.example.moneyconverter.ui.repository

import com.example.moneyconverter.data.room.CurrencyDao
import com.example.moneyconverter.data.room.HistoryDao
import com.example.moneyconverter.data.room.models.Currency
import com.example.moneyconverter.data.room.models.History

class Repository (
    private val currencyDao: CurrencyDao,
    private val historyDao: HistoryDao,
) {
    val currency = currencyDao.getAllCurrencies()
    val history = historyDao.getHistory()

    fun getSingleCurrency(currencyId: String) = currencyDao.getSingleCurrency(currencyId)
    fun getSingleHistory(historyId: Int) = historyDao.getSingleHistory(historyId)

    suspend fun insertCurrency(currency: Currency) {
        currencyDao.insert(currency)
    }

    suspend fun insertHistory(history: History) {
        historyDao.insert(history)
    }
}