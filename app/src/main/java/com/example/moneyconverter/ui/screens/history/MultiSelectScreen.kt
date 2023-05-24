package com.example.moneyconverter.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneyconverter.data.room.models.HistorySettings
import kotlinx.coroutines.flow.merge
import java.time.LocalDate
import java.util.*

@Composable
fun MultiSelectScreen(
    historyViewModel: HistoryViewModel,
    navController: NavController
) {
    val items = historyViewModel.settingsState.currencies
    val selectedItems = historyViewModel.settingsState.settings!!.filterCurrencies

    val settingsState: HistorySettings = historyViewModel.settingsState.settings!!

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(bottom = 80.dp), onClick = { navController.navigate("history/settings") }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            items(items.size) { i ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                val newFilteredList: List<String> =
                                    if (selectedItems.contains(items[i])) {
                                        settingsState.filterCurrencies.filter { str -> str != items[i] }
                                    } else {
                                        settingsState.filterCurrencies + listOf(items[i])
                                    }

                                historyViewModel.updateSettingsState(
                                    settings = settingsState.copy(
                                        filterCurrencies = newFilteredList
                                    )
                                )
                            }
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = items[i])
                    if(selectedItems.contains(items[i])) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.Green,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}