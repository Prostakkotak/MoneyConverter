package com.example.moneyconverter.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "currency")
data class Currency(
    @ColumnInfo(name = "currency_id")
    @PrimaryKey
    val id: String, // UID Валюты, usd или rub например

    val shortName: String, // Имя Валюты в формате XXX (RUB/USD/EUR)
    val fullName: String, // Полное имя, (Российский рубль)
    val isFavorite: Boolean
)

@Entity(tableName = "history")
data class History(
    @ColumnInfo(name = "history_id")
    @PrimaryKey(autoGenerate = true)
    val id:Int,

    val date: Date, // Дата операции

    val fromValue: Int, // Исходник
    val toValue: Int, // Результат

    val fromCurrency: String, // ForeignKey валюты ИЗ которой меняем (Либо кстати просто строка rub, usd, по ней дальше можно просто брать саму валюту)
    val toCurrency: String // ForeignKey валюты В которую меняем
)