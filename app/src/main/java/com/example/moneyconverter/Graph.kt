package com.example.moneyconverter

import android.content.Context
import com.example.moneyconverter.data.room.CurrencyDataBase
import com.example.moneyconverter.ui.repository.Repository

object Graph {
    lateinit var db: CurrencyDataBase
        private set

    val repository by lazy {
        Repository(
            currencyDao = db.currencyDao(),
            historyDao = db.historyDao(),
            exchangeRateDao = db.exchangeRateDao()
        )
    }

    fun provide(context:Context) {
        db = CurrencyDataBase.getDatabase(context)
    }
}