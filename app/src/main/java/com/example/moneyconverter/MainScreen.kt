package com.example.moneyconverter

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.moneyconverter.navigation.BottomBar
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