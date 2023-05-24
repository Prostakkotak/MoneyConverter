package com.example.moneyconverter.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moneyconverter.ui.screens.analytics.AnalyticsScreen
import com.example.moneyconverter.ui.screens.exchange.ExchangeBaseScreen
import com.example.moneyconverter.ui.screens.history.HistoryBaseScreen
import com.example.moneyconverter.ui.screens.history.HistoryScreen

@Composable
fun BottomNavGraph(navController: NavHostController) { // "Ящик" для основных экранов, его содержание определяется текущим route
    NavHost(navController = navController, startDestination = BottomNavBar.ExchangePage.route) {
        composable(route = BottomNavBar.ExchangePage.route) {
            ExchangeBaseScreen()
        }
        composable(route = BottomNavBar.HistoryPage.route) {
            HistoryBaseScreen()
        }
        composable(route = BottomNavBar.AnalyticsPage.route) {
            AnalyticsScreen()
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavBar.HistoryPage,
        BottomNavBar.ExchangePage,
        BottomNavBar.AnalyticsPage
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        screens.forEach() { screen ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.route === screen.route
                } == true,
                label = { Text(screen.title) },
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(screen.icon, screen.title) },
            )
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
        "Главная",
        icon = Icons.Default.Home
    )
    object HistoryPage: BottomNavBar(
        "history",
        "История",
        icon = Icons.Default.List
    )
    object AnalyticsPage: BottomNavBar(
        "analytics",
        "Аналитика",
        icon = Icons.Default.Info
    )
}