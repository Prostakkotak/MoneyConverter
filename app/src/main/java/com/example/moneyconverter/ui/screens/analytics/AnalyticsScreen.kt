package com.example.moneyconverter.ui.screens.analytics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AnalyticsScreen(

) {
    val analyticsViewModel = viewModel(modelClass = AnalyticsViewModel::class.java)
    val historyState = analyticsViewModel.uiState

    Text(text = "Здесь будет аналитика (настройки)")
}