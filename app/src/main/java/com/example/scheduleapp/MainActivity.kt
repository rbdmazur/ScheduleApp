package com.example.scheduleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.scheduleapp.login.LoginScreen
import com.example.scheduleapp.mainscreen.MainScreen
import com.example.scheduleapp.utils.LoginScreenRoute
import com.example.scheduleapp.utils.MainScreenRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = LoginScreenRoute
            ) {
                composable<LoginScreenRoute> {
                    LoginScreen { navController.navigate(MainScreenRoute(it)) }
                }

                composable<MainScreenRoute> {
                    val args = it.toRoute<MainScreenRoute>().id
                    MainScreen(args) {
                        navController.navigate(LoginScreenRoute)
                    }
                }
            }
        }
    }
}
