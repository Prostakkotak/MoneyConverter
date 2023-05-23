package com.example.moneyconverter.ui.screens.history

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneyconverter.data.room.models.History
import com.example.moneyconverter.ui.screens.exchange.ExchangeViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HistoryScreen(
) {
    val historyViewModel = viewModel(modelClass = HistoryViewModel::class.java)
    val historyState = historyViewModel.uiState

    val sortedHistory = historyState.historyList.sortedByDescending { it.date }

    Scaffold() {
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