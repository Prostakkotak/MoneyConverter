package com.example.moneyconverter.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moneyconverter.ui.screens.analytics.AnalyticsScreen
import com.example.moneyconverter.ui.screens.exchange.ExchangeScreen
import com.example.moneyconverter.ui.screens.history.HistoryScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavBar.ExchangePage.route) {
        composable(route = BottomNavBar.ExchangePage.route) {
            ExchangeScreen()
        }
        composable(route = BottomNavBar.HistoryPage.route) {
            HistoryScreen()
        }
        composable(route = BottomNavBar.AnalyticsPage.route) {
            AnalyticsScreen()
        }
    }
}

sealed class BottomNavBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object ExchangePage: BottomNavBar(
        "exchange",
        "Список валют",
        icon = Icons.Default.List
    )
    object HistoryPage: BottomNavBar(
        "history",
        "История операций",
        icon = Icons.Default.Search
    )
    object AnalyticsPage: BottomNavBar(
        "analytics",
        "Аналитика",
        icon = Icons.Default.Info
    )
}