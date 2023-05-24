package com.example.moneyconverter.ui.screens.history

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneyconverter.data.room.models.History
import com.example.moneyconverter.data.room.models.HistorySettings
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HistoryBaseScreen() {
    val historyNavController = rememberNavController()
    val historyViewModel = viewModel(modelClass = HistoryViewModel::class.java)

    Scaffold() {
        NavGraph(navController = historyNavController, historyViewModel = historyViewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel,
    navController: NavController
) {
    val historyState: HistoryState = historyViewModel.uiState
    val historyFilters: HistorySettings? = historyViewModel.settingsState.settings

    val sortedHistory = historyState.historyList.filter {history ->
        if (historyFilters != null) filterHistory(history = history, filters = historyFilters) else true
    }.sortedByDescending { it.date }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("history/settings") },
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "")
            }
        }
    ) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)) {
            items(sortedHistory.size) {index ->
                val history: History = sortedHistory[index]

                HistoryCard(history = history)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryCard(history: History, modifier: Modifier = Modifier) {

    val dateTimeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")

    Card(modifier = modifier
        .fillMaxWidth()
        .padding(top = 8.dp)) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = history.fromCurrency, fontSize = 24.sp)
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "")
                Text(text = history.toCurrency, fontSize = 24.sp)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = history.fromValue.toString())
                Text(text = history.toValue.toString())
            }
            Text(text = dateTimeFormat.format(history.date), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

fun filterHistory(history: History, filters: HistorySettings): Boolean {

    // Проверки на попадание в промежуток дат
    if (filters.periodType == "week" && !isDateInRange(
            startDate = getLastWeekDateRange()[0],
            endDate = getLastWeekDateRange()[1],
            date = history.date
    )) {
        return false
    } else if (filters.periodType == "month" && !isDateInRange(
            startDate = getLastMonthDateRange()[0],
            endDate = getLastMonthDateRange()[1],
            date = history.date
    )) {
        return false
    } else if (filters.periodType == "custom" && !isDateInRange(
            startDate = filters.customPeriodStart!!,
            endDate = filters.customPeriodEnd!!,
            date = history.date
    )) {
        return false
    }

    // Проверяем есть ли в списке фильтрованных валют если он включен
    if (
        filters.filterCurrencies.size > 0
        && !filters.filterCurrencies.contains(history.toCurrency) && !filters.filterCurrencies.contains(history.fromCurrency)
    ) {
        return false
    }

    return true
}

fun getLastMonthDateRange(): List<Date> {
    val calendar = Calendar.getInstance()
    val endDate = calendar.time

    calendar.add(Calendar.MONTH, -1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.add(Calendar.DATE, -1)
    val startDate = calendar.time

    return listOf(startDate, endDate)
}

fun getLastWeekDateRange(): List<Date> {
    val calendar = Calendar.getInstance()
    val endDate = calendar.time

    calendar.add(Calendar.DATE, -6)
    val startDate = calendar.time

    return listOf(startDate, endDate)
}

fun isDateInRange(date: Date, startDate: Date, endDate: Date): Boolean {
    return date >= startDate && date <= endDate
}