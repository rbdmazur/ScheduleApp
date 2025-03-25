package com.example.scheduleapp.mainscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scheduleapp.R
import com.example.scheduleapp.data.model.Notification
import com.example.scheduleapp.data.model.Subject
import com.example.scheduleapp.mainscreen.notificationdialog.NotificationDialog
import com.example.scheduleapp.ui.theme.blackAlpha
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.whiteAlpha
import com.example.scheduleapp.utils.DateFormats
import java.util.Date

private const val TAG = "SUBJECTS"

@Composable
fun SubjectsScreen(viewModel: MainViewModel, modifier: Modifier) {
    var isNotificationDialogShown by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        val subjects by viewModel.subjects.collectAsState()
        val notifications by viewModel.notifications.collectAsState()
        try {
            viewModel.loadSubjects()
            viewModel.loadNotifications()
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
        SubjectsRow(subjects)
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.subjects_notification),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { isNotificationDialogShown = true }) {
                Icon(
                    painterResource(R.drawable.baseline_notification_add_24),
                    "Add notification",
                    tint = Color.Black
                )
            }
        }
        if (notifications.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.subjects_notification_alert),
                    color = blackAlpha,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Icon(
                    painterResource(R.drawable.baseline_notification_add_24),
                    "",
                    tint = blackAlpha
                )
            }
        } else {
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Black)
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(notifications) { index, item ->
                    if (index != 0) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Black)
                    }
                    val subject = subjects.firstOrNull { it.id == item.subjectId }
                    subject?.let { NotificationLazyColumnItem(item, it) }
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Black)
        }
    }

    if (isNotificationDialogShown) {
        NotificationDialog(viewModel) {
            isNotificationDialogShown = false
        }
    }
}

@Composable
fun NotificationLazyColumnItem(item: Notification, subject: Subject) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = item.title,
                fontSize = 16.sp
            )
            Text(
                text = subject.shortTitle,
                fontSize = 12.sp
            )
        }
        Text(
            text = DateFormats.notificationDateFormat.format(Date(item.date)),
            fontSize = 16.sp
        )
    }
}

@Composable
fun SubjectsRow(subjects: List<Subject>) {
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
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
//            Text(
//                text = stringResource(R.string.subjects_subjects_title),
//                fontSize = 24.sp,
//                color = Color.White,
//                modifier = Modifier.padding(horizontal = 16.dp)
//            )
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                itemsIndexed(subjects) { index, item ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    LazyRowSubjectItem(item)
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

@Composable
fun LazyRowSubjectItem(item: Subject) {
    Box(
        modifier = Modifier
            .width(240.dp)
            .height(140.dp)
            .background(darkBlue, RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = item.title,
            color = whiteAlpha,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}