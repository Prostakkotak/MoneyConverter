package com.example.moneyconverter.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "currency")
data class Currency(
    @ColumnInfo(name = "currency_id")
    @PrimaryKey
    val id: String, // UID Валюты, usd или rub например

    val shortName: String, // Имя Валюты в формате XXX (RUB/USD/EUR)
    val fullName: String, // Полное имя, (Российский рубль)
    val isFavorite: Boolean,
    val lastTimeUsed: Date
)

@Entity(tableName = "history")
data class History(
    @ColumnInfo(name = "history_id")
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,

    val date: Date, // Дата операции

    val fromValue: Double, // Исходник
    val toValue: Double, // Результат

    val fromCurrency: String, // ForeignKey валюты ИЗ которой меняем (Либо кстати просто строка rub, usd, по ней дальше можно просто брать саму валюту)
    val toCurrency: String // ForeignKey валюты В которую меняем
)

@Entity(tableName = "exchange_rate")
data class ExchangeRate(
    @ColumnInfo(name = "exchange_rate_id")
    @PrimaryKey
    val id: String,
    val value: Double
)