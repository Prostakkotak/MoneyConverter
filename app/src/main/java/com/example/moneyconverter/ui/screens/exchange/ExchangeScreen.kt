package com.example.moneyconverter.ui.screens.exchange

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun ExchangeScreen(
) {
    val exchangeViewModel = viewModel(modelClass = ExchangeViewModel::class.java)
    val exchangeState = exchangeViewModel.uiState
    
    Text(text = "Здесь будет список валют")
}