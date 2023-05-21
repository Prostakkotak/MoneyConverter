package com.example.moneyconverter

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.moneyconverter.navigation.BottomNavBar
import com.example.moneyconverter.navigation.BottomNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // Оно иначе ругается на padding, бесит
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()


    Scaffold(
        bottomBar = { BottomBar(navController = navController)}
    ) {
        BottomNavGraph(navController = navController)
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