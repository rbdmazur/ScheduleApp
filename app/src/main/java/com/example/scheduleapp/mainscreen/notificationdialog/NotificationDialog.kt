package com.example.scheduleapp.mainscreen.notificationdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.scheduleapp.R
import com.example.scheduleapp.data.model.Subject
import com.example.scheduleapp.mainscreen.MainViewModel
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.gold
import com.example.scheduleapp.ui.theme.redError
import com.example.scheduleapp.ui.theme.whiteAlpha
import com.example.scheduleapp.utils.DateFormats
import java.util.Calendar
import java.util.Date

@Composable
fun NotificationDialog(
    viewModel: MainViewModel,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = blue,
                contentColor = Color.White
            )
        ) {
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current
            var errorDate by remember { mutableStateOf(false) }
            var showErrorDialog by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.fillMaxWidth().clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.notifications_title),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    color = whiteAlpha
                )
                val notificationTitle = remember { mutableStateOf("") }
                NotificationTitleRow(notificationTitle)
                val notificationSubject = remember { mutableStateOf<Subject?>(null) }
                val subjects by viewModel.subjects.collectAsState()
                NotificationSubjectRow(subjects, notificationSubject)
                val notificationDate = remember { mutableStateOf<Long?>(null) }
                NotificationDateRow(notificationDate, { errorDate = false }) {
                    errorDate = true
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    color = whiteAlpha
                )
                Box(
                    modifier = Modifier.fillMaxWidth().clickable {
                        if (notificationSubject.value != null && notificationDate.value != null && !errorDate) {
                            viewModel.addNotification(
                                notificationSubject.value!!,
                                notificationTitle.value,
                                notificationDate.value!!
                            )
                        }
                        if (errorDate) {
                            showErrorDialog = true
                        } else {
                            onDismissRequest()
                        }
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.dialog_add),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (showErrorDialog) {
                ErrorDialog {
                    showErrorDialog = false
                }
            }
        }
    }
}

@Composable
fun ErrorDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = blue,
                contentColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.notification_error),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun NotificationDateRow(notificationDate: MutableState<Long?>, onNormalDate: () -> Unit, onInvalidDate: () -> Unit) {
    var datePickerShown by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.notification_date),
            modifier = Modifier.padding(end = 16.dp)
        )

        Box(
            modifier = Modifier.width(200.dp).height(50.dp).clickable { datePickerShown = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (notificationDate.value == null) {
                    stringResource(R.string.notification_select_date)
                } else {
                    DateFormats.notificationDateFormat.format(Date(notificationDate.value!!))
                },
                textAlign = TextAlign.Center
            )
        }
    }
    if (datePickerShown) {
        DatePickerModal(
            onDateSelected = {
                notificationDate.value = it
                val calendar = Calendar.getInstance()
                val currentDate = calendar.time
                if (it != null && Date(it).before(currentDate)) {
                    onInvalidDate()
                } else {
                    onNormalDate()
                }
            },
            onDismiss = { datePickerShown = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.dialog_cancel), color = Color.White)
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = blue,
                titleContentColor = Color.White,
                headlineContentColor = Color.White,
                weekdayContentColor = Color.White,
                subheadContentColor = Color.White,
                navigationContentColor = Color.White,
                yearContentColor = Color.White,
                disabledYearContentColor = Color.White,
                currentYearContentColor = Color.White,
                todayDateBorderColor = darkBlue,
                todayContentColor = Color.White,
                selectedDayContainerColor = darkBlue,
                selectedDayContentColor = Color.White,
                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedIndicatorColor = gold,
                    unfocusedIndicatorColor = whiteAlpha,
                    focusedPlaceholderColor = whiteAlpha,
                    unfocusedPlaceholderColor = whiteAlpha,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedLabelColor = gold,
                    unfocusedLabelColor = whiteAlpha,

                    errorIndicatorColor = redError,
                    errorLabelColor = redError,
                    errorContainerColor = Color.Transparent
                )
            )
        )
    }
}

@Composable
fun NotificationSubjectRow(subjects: List<Subject>, notificationSubject: MutableState<Subject?>) {
    var showPopUp by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.notification_subject),
            modifier = Modifier.padding(end = 16.dp)
        )

        Box(
            modifier = Modifier.width(200.dp).height(80.dp).clickable {
                showPopUp = !showPopUp
            },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (notificationSubject.value == null) {
                    stringResource(R.string.notification_select_subject)
                } else {
                    notificationSubject.value!!.shortTitle
                },
                textAlign = TextAlign.Center
            )

        }

        val scrollState = rememberScrollState()
        if (showPopUp) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { showPopUp = false },
                properties = PopupProperties(
                    excludeFromSystemGesture = true
                ),
                offset = IntOffset(130, 510)
            ) {
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .heightIn(max = 250.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .verticalScroll(scrollState)
                ) {
                    subjects.forEachIndexed { index, item ->
                        if (index != 0) {
                            HorizontalDivider(color = Color.White, modifier = Modifier.fillMaxWidth())
                        }
                        Box(
                            modifier = Modifier
                                .background(darkBlue)
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    showPopUp = !showPopUp
                                    notificationSubject.value = item
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.shortTitle, color = whiteAlpha, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationTitleRow(notificationTitle: MutableState<String>) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.notification_title),
            modifier = Modifier.padding(end = 16.dp)
        )

        Box(
            modifier = Modifier.width(200.dp).padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = notificationTitle.value,
                onValueChange = { notificationTitle.value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    focusedIndicatorColor = gold,
                    unfocusedTextColor = whiteAlpha,
                    unfocusedIndicatorColor = whiteAlpha,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}