package com.example.moneyconverter.network

import android.util.Log
import com.example.moneyconverter.ui.repository.Repository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.json.JSONObject

class API() {
    private val apiKey = "pnGlxQiYJyVQ0rQ94mn5vIOBaBXHceTWNQbsXaG1"
    private val baseUrl = "https://api.currencyapi.com/v3/"
    private val client = HttpClient()

    suspend fun getCurrencies(): JSONObject {
        val response: HttpResponse = client.request("$baseUrl/currencies") {
            method = HttpMethod.Get
            headers {
                append("apikey", apiKey)
            }
        }

        lateinit var currenciesJson: JSONObject

        if (response.status.value in 200..299) {
            Log.d("KTOR", "Successful /currencies response")

            currenciesJson = JSONObject(response.bodyAsText()).getJSONObject("data") // Да я делаю это костыльно, просто ненавижу сериализацию и подобные вещи
        }

        return currenciesJson
    }

    suspend fun getExchangeRates(): JSONObject { // Тянем с API курс обмена всех валют к USD, относительно него будем всё считать
        val response: HttpResponse = client.request("$baseUrl/latest") {
            method = HttpMethod.Get
            headers {
                append("apikey", apiKey)
            }
        }

        lateinit var exchangeRatesJson: JSONObject

        if (response.status.value in 200..299) {
            Log.d("KTOR", "Successful /latest response")

            exchangeRatesJson = JSONObject(response.bodyAsText()).getJSONObject("data")
        }

        return exchangeRatesJson
    }
}