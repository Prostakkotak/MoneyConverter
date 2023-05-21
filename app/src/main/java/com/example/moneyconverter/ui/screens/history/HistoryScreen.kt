package com.example.moneyconverter.ui.screens.history

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneyconverter.ui.screens.exchange.ExchangeViewModel

@Composable
fun HistoryScreen(
) {
    val historyViewModel = viewModel(modelClass = HistoryViewModel::class.java)
    val historyState = historyViewModel.uiState

    Text(text = "Здесь будет история операций")
}