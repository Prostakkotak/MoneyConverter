package com.example.moneyconverter.data.room

import androidx.room.*
import com.example.moneyconverter.data.room.models.Currency
import com.example.moneyconverter.data.room.models.ExchangeRate
import com.example.moneyconverter.data.room.models.History
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao { // Валюты
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currency: Currency)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(currency: Currency)

    @Delete
    suspend fun delete(currency: Currency)

    @Query("SELECT * FROM currency") // Список ВСЕХ валют
    fun getAllCurrencies(): Flow<List<Currency>> // Для юзания liveData тут нужен Flow (так сказал индус из видео, я ему верю)

    @Query("SELECT * FROM currency WHERE currency_id =:currencyId") // Тянем с БД ОДНУ валюту по uid (usd, rub итд)
    fun getSingleCurrency(currencyId:String): Flow<Currency>
}

@Dao
interface HistoryDao { // Истории операций
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeHistory: History)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exchangeHistory: History)

    @Delete
    suspend fun delete(exchangeHistory: History)

    @Query("SELECT * FROM history") // Список ВСЕХ историй
    fun getHistory(): Flow<List<History>>

    @Query("SELECT * FROM history WHERE history_id =:historyId") // Тянем с БД ОДНУ историю по id
    fun getSingleHistory(historyId:Int): Flow<History>
}

@Dao
interface ExchangeRateDao { // Курсы обмена относительно USD
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRate)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exchangeRate: ExchangeRate)

    @Delete
    suspend fun delete(exchangeRate: ExchangeRate)

    @Query("SELECT * FROM exchange_rate")
    fun getExchangeRates(): Flow<List<ExchangeRate>>

    @Query("SELECT * FROM exchange_rate WHERE exchange_rate_id =:exchangeRateId")
    fun getSingleExchangeRate(exchangeRateId: String): Flow<ExchangeRate>
}