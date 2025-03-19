package com.example.scheduleapp.mainscreen.barscontent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.R
import com.example.scheduleapp.mainscreen.dialogs.AddDialog
import com.example.scheduleapp.mainscreen.MainViewModel
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.gold

@Composable
fun DrawerMenu(viewModel: MainViewModel, authError: () -> Unit) {
    val state by viewModel.mainUiState.collectAsState()
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        ModalDrawerSheet(
            modifier = Modifier.fillMaxWidth(0.7f),
            drawerContainerColor = darkBlue,
            drawerContentColor = Color.White
        ) {
            var showDialog by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val studentName = state.currentStudent?.name?.split(" ")?.get(1)
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.drawer_hello) + " $studentName", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                HorizontalDivider(color = gold)
                state.usersSchedules.forEachIndexed { index, item ->
                    ScheduleMenuItem(
                        item.title,
                        state.currentScheduleIndex == index,
                        state.mainScheduleId == item.scheduleId
                    ) {
                        viewModel.updateSelectedSchedule(index)
                    }
                }
                HorizontalDivider(color = gold)
                Box(modifier = Modifier.fillMaxWidth().clickable {
                    showDialog = true
                },
                contentAlignment = Alignment.Center) {
                    Text(
                        text = "Add schedule",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(20.dp),
                        color = Color.White
                    )
                }

                if(showDialog) {
                    AddDialog(mainViewModel = viewModel, onAddClicked = { showDialog = false }, authError = authError) {
                        showDialog = false
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleMenuItem(title: String, isSelected: Boolean, isMain: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) gold else Color.White
    Box(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                color = color
            )
            if (isMain) {
                Icon(painterResource(R.drawable.baseline_star_24), "", tint = color)
            }
        }
    }
}