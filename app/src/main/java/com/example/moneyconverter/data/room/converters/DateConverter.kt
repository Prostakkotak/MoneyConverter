package com.example.moneyconverter.data.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

open class DateConverter {

    @TypeConverter
    fun toDate(date: Long?): Date? {
        return date?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

}

open class StringListTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }
}