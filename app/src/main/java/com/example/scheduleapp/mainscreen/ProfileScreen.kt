package com.example.scheduleapp.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduleapp.R
import com.example.scheduleapp.data.model.StudentWithEmailAndInfo
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.whiteAlpha

@Composable
fun ProfileScreen(modifier: Modifier, viewModel: MainViewModel) {
    val mainUiState by viewModel.mainUiState.collectAsState()
    val studentInfo by viewModel.studentInfo.collectAsState()
    viewModel.loadStudentInfo()
    Column(
        modifier = modifier
    ) {
        studentInfo?.let {
            UserBox(it)
        }
        SettingsBox()
    }
}

@Composable
fun UserBox(student: StudentWithEmailAndInfo) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
            )
            .background(blue)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                text = student.student.name,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = student.user.email,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            val info = StringBuilder()
            info.append(student.info.specialization)
            info.append(" - ")
            info.append(student.info.course.toString() + " " + stringResource(R.string.profile_course))
            info.append(" - ")
            info.append(student.info.group.toString() + " " + stringResource(R.string.profile_group))
            Text(
                text = info.toString(),
                color = whiteAlpha,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun SettingsBox() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.profile_settings),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                painterResource(R.drawable.baseline_settings_24),
                "settings",
                tint = Color.Black
            )
        }
//        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), color = Color.Black)
    }
}