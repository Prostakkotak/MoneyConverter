package com.example.moneyconverter.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerScreen(
    historyViewModel: HistoryViewModel,
    navController: NavController
) {
    val settingsState = historyViewModel.settingsState.settings!!

    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = settingsState.customPeriodStart?.time,
        initialSelectedEndDateMillis = settingsState.customPeriodEnd?.time
    )

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        // Add a row with "Save" and dismiss actions.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.navigate("history/settings") }) {
                Icon(Icons.Filled.Close, contentDescription = "Localized description")
            }
            TextButton(
                onClick = {
                    historyViewModel.updateSettingsState(settingsState.copy(
                        customPeriodStart = Date(state.selectedStartDateMillis!!),
                        customPeriodEnd = Date(state.selectedEndDateMillis!!)
                    ))

                    navController.navigate("history/settings")
                },
                enabled = state.selectedEndDateMillis != null
            ) {
                Text(text = "Сохранить")
            }
        }

        DateRangePicker(state = state, modifier = Modifier.weight(1f))
    }
}