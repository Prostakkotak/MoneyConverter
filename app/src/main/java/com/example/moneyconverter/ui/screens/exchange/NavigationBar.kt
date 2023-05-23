package com.example.moneyconverter.ui.screens.exchange

import android.content.res.Resources
import android.provider.CalendarContract
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TopNavGraph(navController: NavHostController, exchangeState: ExchangeState, exchangeViewModel: ExchangeViewModel) { // "Ящик" для экранов валют (список и конвертация)
    NavHost(navController = navController, startDestination = TopNavBar.ExchangePage.route) {
        composable(route = TopNavBar.ExchangePage.route) {
            ExchangeScreen(
                exchangeViewModel = exchangeViewModel,
                exchangeState = exchangeState,
                navController = navController
            )
        }
        composable(route = TopNavBar.ConvertationPage.route) {
            ConvertationScreen(
                exchangeViewModel = exchangeViewModel,
                exchangeState = exchangeState,
                navController = navController
            )
        }
    }
}

@Composable
fun TopBar(navController: NavHostController, exchangeState: ExchangeState) {
    val screens = listOf(
        TopNavBar.ExchangePage,
        TopNavBar.ConvertationPage,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var selectedTabIndex = 0

    screens.forEachIndexed() {index, screen -> // Считаем индекс активной табы
        if (currentDestination?.hierarchy?.any {
                it.route === screen.route
            } == true) selectedTabIndex = index
    }

    val isConvertationCurrenciesSet: Boolean = exchangeState.fromCurrency != null && exchangeState.toCurrency != null

    TabRow(selectedTabIndex = selectedTabIndex) {
        screens.forEach { screen ->
            Tab(
                selected = currentDestination?.hierarchy?.any {
                    it.route === screen.route
                } == true,
                enabled = isConvertationCurrenciesSet,
                onClick = {
                    navController.navigate(screen.route)
            }) {
                Text(
                    text = screen.title,
                    modifier = Modifier.padding(top = 16.dp, bottom = 18.dp),
                )
            }
        }
    }
}

sealed class TopNavBar(
    val route: String,
    val title: String,
) {
    object ExchangePage: TopNavBar(
        "exchange",
        "Валюты",
    )
    object ConvertationPage: TopNavBar(
        "convertation",
        "Конвертация",
    )
}