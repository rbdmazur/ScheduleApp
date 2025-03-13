package com.example.scheduleapp.mainscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scheduleapp.mainscreen.barscontent.DrawerMenu
import com.example.scheduleapp.mainscreen.barscontent.TopBar
import com.example.scheduleapp.ui.theme.ScheduleAppTheme
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.gold
import com.example.scheduleapp.ui.theme.whiteAlpha
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

@Composable
fun MainScreen(idStr: String) {
    val id = UUID.fromString(idStr)

    val mainViewModel = hiltViewModel<MainViewModel, MainViewModel.Factory>(
        creationCallback = { factory -> factory.create(id = id) }
    )
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ScheduleAppTheme {
        ModalNavigationDrawer(
            modifier = Modifier.fillMaxSize(),
            drawerContent = { DrawerMenu(mainViewModel) },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopBar(mainViewModel) {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                }
            ) { innerPadding ->
                MainScreenContent(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    mainViewModel
                )
            }
        }
    }
}

@Composable
fun MainScreenContent(modifier: Modifier, mainViewModel: MainViewModel) {
    val state by mainViewModel.mainUiState.collectAsState()
    val dateState by mainViewModel.dateState.collectAsState()
    Column(modifier = modifier) {
        DayRow(dateState) {index ->
            mainViewModel.updateSelectedDay(index)
        }
    }
}

@Composable
fun DayRow(state: DateState,  onItemClicked: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
            )
            .background(blue)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.dates.forEachIndexed { index, date ->
                DayRowItem(date, index == state.selectedDay) { onItemClicked(index)  }
            }
        }
    }
}

@Composable
fun DayRowItem(date: Date, isSelected: Boolean, onItemClicked: () -> Unit) {
    val textSize = 24.sp
    val dateStr = DateFormat.dateRowFormat.format(date)
    val color = if (isSelected) gold else whiteAlpha
    val splited = dateStr.split(", ")
    val dayNum = splited[0]
    //Take first letter
    val dayWeek = splited[1][0].toString()
    Column(
        modifier = Modifier.clickable { onItemClicked() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(color = darkBlue, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                dayNum,
                color = color,
                fontSize = textSize,
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
            dayWeek,
            color = color,
            fontSize = textSize,
            modifier = Modifier.padding(8.dp)
        )
    }
}