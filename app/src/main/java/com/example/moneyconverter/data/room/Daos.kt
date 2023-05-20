package com.example.moneyconverter.data.room

import androidx.room.*
import com.example.moneyconverter.data.room.models.Currency
import com.example.moneyconverter.data.room.models.History
import kotlinx.coroutines.flow.Flow


// Вообще вся бд реализована благодаря этому чуваку, поклон ему в ноги
// https://www.youtube.com/watch?v=D7PW4P3FmnU&ab_channel=HoodLab

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

    @Query("SELECT * FROM ") // Список ВСЕХ историй
    fun getHistory(): Flow<List<Currency>>

    @Query("SELECT * FROM history WHERE history_id =:historyId") // Тянем с БД ОДНУ историю по id
    fun getSingleHistory(historyId:Int): Flow<Currency>
}