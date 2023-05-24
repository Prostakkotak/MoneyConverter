package com.example.moneyconverter.ui.screens.history

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController, historyViewModel: HistoryViewModel) {
    NavHost(navController = navController, startDestination = "history") {
        composable(route = "history") {
            HistoryScreen(
                historyViewModel = historyViewModel,
                navController = navController
            )
        }
        composable(route = "history/settings") {
            HistorySettingsScreen(
                historyViewModel = historyViewModel,
                navController = navController
            )
        }
        composable(route="history/settings/period") {
            DateRangePickerScreen(
                historyViewModel = historyViewModel,
                navController = navController
            )
        }
        composable(route="history/settings/multi_select") {
            MultiSelectScreen(historyViewModel = historyViewModel, navController = navController)
        }
    }
}