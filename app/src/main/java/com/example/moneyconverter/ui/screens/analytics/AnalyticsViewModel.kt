package com.example.moneyconverter.ui.screens.analytics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.moneyconverter.Graph
import com.example.moneyconverter.ui.repository.Repository

class AnalyticsViewModel(
    private val repository: Repository = Graph.repository
): ViewModel() {
    var uiState by mutableStateOf(AnalyticsState())
        private set

    init {

    }
}

data class AnalyticsState(
    val period: String = "",
)