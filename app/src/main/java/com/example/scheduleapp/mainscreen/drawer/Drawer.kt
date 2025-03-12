package com.example.scheduleapp.mainscreen.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scheduleapp.R
import com.example.scheduleapp.mainscreen.MainViewModel
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.gold

@Composable
fun DrawerMenu(viewModel: MainViewModel) {
    val state by viewModel.mainUiState.collectAsState()
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.7f),
        drawerContainerColor = darkBlue,
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val studentName = state.currentStudent?.name?.split(" ")?.get(1)
            Spacer(Modifier.height(12.dp))
            Text(stringResource(R.string.drawer_hello) + studentName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
            HorizontalDivider(color = gold)
            Text("Schedule 1", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
            Spacer(Modifier.height(16.dp))
        }
    }
}