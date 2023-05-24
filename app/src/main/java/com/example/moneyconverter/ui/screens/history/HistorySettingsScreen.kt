package com.example.moneyconverter.ui.screens.history

import android.annotation.SuppressLint
import android.util.Log
import android.widget.DatePicker
import android.widget.RadioGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.example.moneyconverter.data.room.models.HistorySettings
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HistorySettingsScreen(
    historyViewModel: HistoryViewModel,
    navController: NavController
) {
    val historyState: HistoryState = historyViewModel.uiState
    val settingsState: HistorySettings = historyViewModel.settingsState.settings!!

    historyViewModel.getCurrencies()
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 80.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                RadioWithLabel(label = "За всё время", selected = settingsState.periodType == "alltime", onClick = {
                    historyViewModel.updateSettingsState(
                        settings = settingsState.copy(
                            periodType = "alltime"
                        )
                    )
                })
                RadioWithLabel(label = "За неделю", selected = settingsState.periodType == "week", onClick = {
                    historyViewModel.updateSettingsState(
                        settings = settingsState.copy(
                            periodType = "week"
                        )
                    )
                })
                RadioWithLabel(label = "За месяц", selected = settingsState.periodType == "month", onClick = {
                    historyViewModel.updateSettingsState(
                        settings = settingsState.copy(
                            periodType = "month"
                        )
                    )
                })
                RadioWithLabel(label = "Свой период", selected = settingsState.periodType == "custom", onClick = {
                    historyViewModel.updateSettingsState(
                        settings = settingsState.copy(
                            periodType = "custom"
                        )
                    )
                })

                if (settingsState.periodType == "custom") {
                    Button(
                        onClick = { navController.navigate("history/settings/period") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20)
                    ) {
                        if (settingsState.customPeriodEnd != null) {
                            val dateTimeFormat = SimpleDateFormat("dd.MM.yyyy")

                            Row {
                                Text(text = "${(dateTimeFormat.format(settingsState.customPeriodStart))} - ")
                                Text(text = dateTimeFormat.format(settingsState.customPeriodEnd))
                            }
                        } else {
                            Text(text = "Выбрать свой период")
                        }
                    }
                }

                Button(
                    onClick = {
                        navController.navigate("history/settings/multi_select")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20),) {
                    Text(text = "Фильтровать по валютам" )
                }
            }

            Column {
                Button(
                    onClick = {
                        historyViewModel.saveHistorySettings(settingsState)
                        navController.navigate("history")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20),
                    enabled =
                    settingsState.periodType != historyState.settings!!.periodType
                            || (settingsState.periodType == "custom" && settingsState.customPeriodEnd != historyState.settings!!.customPeriodEnd)
                            || (settingsState.periodType == "custom" && settingsState.customPeriodStart != historyState.settings!!.customPeriodStart)
                            || !settingsState.filterCurrencies.equals(historyState.settings!!.filterCurrencies)
                ) {
                    Text(text = "Применить")
                }
                Button(
                    onClick = {
                        historyViewModel.saveHistorySettings(HistorySettings("main"))
                        navController.navigate("history")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20),
                ) {
                    Text(text = "Сбросить")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioWithLabel(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(modifier = Modifier.clickable(onClick = onClick), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label)
    }
}