package com.example.scheduleapp.mainscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scheduleapp.mainscreen.drawer.DrawerMenu
import com.example.scheduleapp.ui.theme.ScheduleAppTheme
import dagger.hilt.android.lifecycle.withCreationCallback
import java.util.UUID

@Composable
fun MainScreen(idStr: String) {
    val id = UUID.fromString(idStr)

    val mainViewModel = hiltViewModel<MainViewModel, MainViewModel.Factory>(
        creationCallback = { factory -> factory.create(id = id) }
    )

    val mainUiState by mainViewModel.mainUiState.collectAsState()
    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerContent = { DrawerMenu(mainViewModel) }
    ) {
        ScheduleAppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                innerPadding
            }
        }
    }
}