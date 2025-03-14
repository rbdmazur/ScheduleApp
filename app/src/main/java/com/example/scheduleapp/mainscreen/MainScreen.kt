package com.example.scheduleapp.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scheduleapp.R
import com.example.scheduleapp.data.model.StudyWithTeacherAndSubject
import com.example.scheduleapp.mainscreen.barscontent.DrawerMenu
import com.example.scheduleapp.mainscreen.barscontent.TopBar
import com.example.scheduleapp.ui.theme.ScheduleAppTheme
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.gold
import com.example.scheduleapp.ui.theme.whiteAlpha
import com.example.scheduleapp.utils.DaysOfWeek
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID
import java.util.stream.Collectors

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
        ScheduleContent(state.currentStudies, dateState)
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

@Composable
fun ScheduleContent(studies: List<StudyWithTeacherAndSubject>, dateState: DateState) {
    val currentDay = dateState.run { dates[selectedDay] }
    val calendar = Calendar.getInstance()
    calendar.time = currentDay
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val studiesForDay = studies.prepareForLazyColumn(day)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(studiesForDay) {
            if (it.second == null) {
                ScheduleItem(it.first)
            } else {
                ScheduleDoubleItem(it.first, it.second!!)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun List<StudyWithTeacherAndSubject>
        .prepareForLazyColumn(day: Int): List<Pair<StudyWithTeacherAndSubject, StudyWithTeacherAndSubject?>> {
    val studiesForDay = this.stream().filter { study ->
        DaysOfWeek.valueOf(study.study.day) == convertToLocalDaysOfWeek(day)
    }.sorted { s1, s2 ->
        s1.study.number.compareTo(s2.study.number)
    }.collect(Collectors.toList())

    var i = 0
    val end = studiesForDay.size
    val result = ArrayList<Pair<StudyWithTeacherAndSubject, StudyWithTeacherAndSubject?>>()
    while (i < end) {
        if (i != end - 1 && studiesForDay[i].study.time == studiesForDay[i + 1].study.time) {
            result.add(Pair(studiesForDay[i++], studiesForDay[i]))
        } else {
            result.add(Pair(studiesForDay[i], null))
        }
        i++
    }
    return result
}

@Composable
fun ScheduleDoubleItem(st1: StudyWithTeacherAndSubject, st2: StudyWithTeacherAndSubject) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = st1.study.time,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier.width(300.dp).heightIn(min = 100.dp, max = 130.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ScheduleDoubleItemLittleBox(st1)
            ScheduleDoubleItemLittleBox(st2)
        }
    }
}

@Composable
fun ScheduleDoubleItemLittleBox(study: StudyWithTeacherAndSubject) {
    Box(
        Modifier.width(146.dp).heightIn(min = 100.dp, max = 130.dp).background(color = blue, shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 130.dp).padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(study.subject.shortTitle)
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 10.sp,
                            color = whiteAlpha
                        )
                    ) {
                        val type = if (study.study.type == "LECTURE") "Lecture" else "Practice"
                        append(" - $type")
                    }
                },
                modifier = Modifier.wrapContentSize().align(Alignment.Start)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(R.drawable.baseline_school_16), "", tint = whiteAlpha)
                    Text(
                        text = study.teacher.name,
                        fontSize = 10.sp,
                        color = whiteAlpha,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Text(
                    text = "Aud. ${study.study.auditorium}",
                    fontSize = 10.sp,
                    color = whiteAlpha
                )
            }
        }
    }
}

@Composable
fun ScheduleItem(study: StudyWithTeacherAndSubject) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = study.study.time,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
        )

        Box(
            modifier = Modifier.width(300.dp).heightIn(min = 100.dp, max = 130.dp).background(color = blue, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 130.dp).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(study.subject.title)
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 10.sp,
                                color = whiteAlpha
                            )
                        ) {
                            val type = if (study.study.type == "LECTURE") "Lecture" else "Practice"
                            append(" - $type")
                        }
                    },
                    modifier = Modifier.wrapContentSize().align(Alignment.Start)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(R.drawable.baseline_school_16), "", tint = whiteAlpha)
                        Text(
                            text = study.teacher.name,
                            fontSize = 10.sp,
                            color = whiteAlpha,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Text(
                        text = "Aud. ${study.study.auditorium}",
                        fontSize = 10.sp,
                        color = whiteAlpha
                    )
                }
            }
        }
    }
}