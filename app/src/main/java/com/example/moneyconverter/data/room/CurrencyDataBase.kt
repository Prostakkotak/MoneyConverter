package com.example.moneyconverter.data.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneyconverter.data.room.converters.DateConverter
import com.example.moneyconverter.data.room.models.Currency
import com.example.moneyconverter.data.room.models.History

// Вообще вся бд реализована благодаря этому чуваку, поклон ему в ноги
// https://www.youtube.com/watch?v=D7PW4P3FmnU&ab_channel=HoodLab

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [Currency::class, History::class,],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDataBase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        var INSTANCE:CurrencyDataBase? = null

        // Функция для получения БД, она либо возвращает уже созданную либо создаёт новую
        // (Собственно поэтому INSTANCE у нас nullable)
        // А ещё она Thread-safe (я так понял это про многопоточность)
        fun getDatabase(context: Context):CurrencyDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    CurrencyDataBase::class.java,
                    "currency_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}