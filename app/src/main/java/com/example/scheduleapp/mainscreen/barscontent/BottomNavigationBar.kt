package com.example.scheduleapp.mainscreen.barscontent

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.scheduleapp.R
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.gold
import com.example.scheduleapp.ui.theme.whiteAlpha
import com.example.scheduleapp.utils.ProfileScreenRoute
import com.example.scheduleapp.utils.ScheduleScreenRoute
import com.example.scheduleapp.utils.SubjectsScreenRoute

@Composable
fun BottomNavBar(
    navHostController: NavHostController
) {
    var selectedNavIndex by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf(
        NavigationItem(
            title = stringResource(R.string.nav_schedule),
            icon = painterResource(R.drawable.baseline_calendar_today_24),
            route = ScheduleScreenRoute
        ),
        NavigationItem(
            title = stringResource(R.string.nav_subjects),
            icon = painterResource(R.drawable.baseline_book_24),
            route = SubjectsScreenRoute
        ),
        NavigationItem(
            title = stringResource(R.string.nav_profile),
            icon = painterResource(R.drawable.baseline_person_24),
            route = ProfileScreenRoute
        )
    )
    NavigationBar(
        containerColor = blue
    ) {
       items.forEachIndexed { index, item ->
           NavigationBarItem(
               selected = selectedNavIndex == index,
               onClick = {
                   selectedNavIndex = index
                   navHostController.navigate(item.route)
               },
               icon = { Icon(item.icon, "schedule") },
               label = {
                   Text(
                       text = item.title,
                       color = if (index == selectedNavIndex) gold else whiteAlpha
                   )
               },
               colors = NavigationBarItemDefaults.colors(
                   selectedIconColor = gold,
                   indicatorColor = darkBlue,
                   unselectedIconColor = whiteAlpha
               ),
           )
       }
    }
}

data class NavigationItem(
    val title: String,
    val icon: Painter,
    val route: Any
)