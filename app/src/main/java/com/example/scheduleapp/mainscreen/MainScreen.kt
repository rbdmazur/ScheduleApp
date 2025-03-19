package com.example.scheduleapp.mainscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scheduleapp.mainscreen.barscontent.BottomNavBar
import com.example.scheduleapp.mainscreen.barscontent.DrawerMenu
import com.example.scheduleapp.mainscreen.barscontent.TopBar
import com.example.scheduleapp.ui.theme.ScheduleAppTheme
import com.example.scheduleapp.utils.ProfileScreenRoute
import com.example.scheduleapp.utils.ScheduleScreenRoute
import com.example.scheduleapp.utils.SubjectsScreenRoute
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun MainScreen(
    idStr: String,
    authError: () -> Unit
) {
    val id = UUID.fromString(idStr)

    val mainViewModel = hiltViewModel<MainViewModel, MainViewModel.Factory>(
        creationCallback = { factory -> factory.create(id = id) }
    )
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    ScheduleAppTheme {
        ModalNavigationDrawer(
            modifier = Modifier.fillMaxSize(),
            drawerContent = { DrawerMenu(mainViewModel, authError) },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopBar(mainViewModel) {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                },
                bottomBar = {
                    BottomNavBar(navController)
                }
            ) { innerPadding ->
                val modifier = Modifier.fillMaxSize()
                NavHost(
                    navController = navController,
                    startDestination = ScheduleScreenRoute,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<ScheduleScreenRoute> {
                        ScheduleScreen(
                            modifier = modifier,
                            mainViewModel = mainViewModel
                        )
                    }
                    composable<SubjectsScreenRoute> {
                        SubjectsScreen(modifier = modifier)
                    }
                    composable<ProfileScreenRoute> {
                        ProfileScreen(modifier = modifier)
                    }
                }
            }
        }
    }
}