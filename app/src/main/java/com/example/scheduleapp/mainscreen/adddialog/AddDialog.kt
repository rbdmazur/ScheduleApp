package com.example.scheduleapp.mainscreen.adddialog

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
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
import com.example.scheduleapp.data.model.Faculty
import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.mainscreen.MainViewModel
import com.example.scheduleapp.ui.theme.blue
import com.example.scheduleapp.ui.theme.darkBlue
import com.example.scheduleapp.ui.theme.darkGold
import com.example.scheduleapp.ui.theme.whiteAlpha
import retrofit2.HttpException
import java.util.Locale

@Composable
fun AddDialog(
    mainViewModel: MainViewModel,
    onAddClicked: () -> Unit,
    authError: () -> Unit,
    onDismiss: () -> Unit
) {
    val filterState = remember { mutableStateOf(FilterState()) }
    val dialogState by mainViewModel.dialogState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(filterState.value) {
        try {
            mainViewModel.loadSchedulesForFilter(
                facultyId = filterState.value.faculty?.id,
                course = filterState.value.course,
                group = filterState.value.group
            )
            Log.d("FILTER", filterState.value.toString())
        } catch (e: HttpException) {
            if (e.code() == 401) {
                makeErrorToast(context, "You are not authorized")
                authError()
            } else {
                makeErrorToast(context, "Unknown HttpError with code: ${e.code()}")
            }
        } catch (e: Exception) {
            Log.e("DIALOG", e.message, e)
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = blue,
                contentColor = Color.White
            )
        ) {
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current
            Column(
                modifier = Modifier.fillMaxWidth().clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                horizontalAlignment = Alignment.End
            ) {
                if (!filterState.value.isFilterClicked) {
                    Text(
                        text = stringResource(R.string.dialog_choose),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(color = whiteAlpha, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { filterState.value = filterState.value.copy(isFilterClicked = true) }) {
                            Icon(painterResource(R.drawable.baseline_filter_list_24), "Filter")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (filterState.value.faculty != null) {
                                FilterChip(filterState.value.faculty!!.title) {
                                    filterState.value = filterState.value.copy(faculty = null)
                                }
                            }

                            if (filterState.value.course != null) {
                                FilterChip(filterState.value.course.toString()) {
                                    filterState.value = filterState.value.copy(course = null)
                                }
                            }

                            if (filterState.value.group != null) {
                                FilterChip(filterState.value.group.toString()) {
                                    filterState.value = filterState.value.copy(group = null)
                                }
                            }
                        }
                    }
                    HorizontalDivider(color = whiteAlpha, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
                    ) {
                        items(dialogState.list.size) { i->
                            ScheduleDialogColumnItem(
                                schedule = dialogState.list[i].schedule,
                                isSelected = dialogState.list[i].isSelected
                            ) {
                                mainViewModel.updateDialogSelectedSchedule(
                                    dialogState.list.mapIndexed { index, item ->
                                        if (i == index) {
                                            item.copy(isSelected = !item.isSelected)
                                        } else {
                                            item
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.wrapContentSize().clickable {
                            val schedulesToAdd = ArrayList<Schedule>()
                            dialogState.list.forEach {
                                if (it.isSelected) {
                                    schedulesToAdd.add(it.schedule)
                                }
                            }
                            try {
                                mainViewModel.addSelectedSchedules(schedulesToAdd)
                            } catch (e: HttpException) {
                                if (e.code() == 401) {
                                    makeErrorToast(context, "You are not authorized")
                                } else {
                                    makeErrorToast(context, "Unknown HttpError with code: ${e.code()}")
                                }
                            } catch (e: Exception) {
                                Log.e("DIALOG", e.message, e)
                            }
                            onAddClicked()
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.dialog_add),
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.wrapContentSize().padding(vertical = 16.dp, horizontal = 32.dp)
                        )
                    }
                } else {
                    var faculty by remember { mutableStateOf(filterState.value.faculty) }
                    var course by remember { mutableStateOf(filterState.value.course.toString()) }
                    var group by remember { mutableStateOf(filterState.value.group.toString()) }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.dialog_filter),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            IconButton(onClick = {
                                filterState.value = filterState.value.copy(
                                    faculty = faculty,
                                    course = course.toIntOrNull(),
                                    group = group.toIntOrNull(),
                                    isFilterClicked = false
                                )
                            }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "",  tint = Color.White)
                            }
                        }
                    }
                    HorizontalDivider(color = whiteAlpha, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        val faculties by mainViewModel.faculties.collectAsState()
                        try {
                            mainViewModel.loadFaculties()
                            Log.d("FACULT", faculties.toString())
                        } catch (e: HttpException) {
                            if (e.code() == 401) {
                                makeErrorToast(context, "You are not authorized")
                            } else {
                                makeErrorToast(context, "Unknown HttpError with code: ${e.code()}")
                            }
                        } catch (e: Exception) {
                            Log.e("DIALOG", e.message, e)
                        }
                        FilterRowFaculty(faculties, faculty) { index ->
                            faculty = faculties[index]
                        }
                        val courseLabel = stringResource(R.string.dialog_course)
                        NumberFilterRow(
                            label = courseLabel,
                            placeholder = filterState.value.course?.toString() ?: courseLabel.lowercase()
                        ) {
                            course = it
                        }
                        val groupLabel = stringResource(R.string.dialog_group)
                        NumberFilterRow(
                            label = groupLabel,
                            placeholder = filterState.value.group?.toString() ?: groupLabel.lowercase()
                        ) {
                            group = it
                        }
                        HorizontalDivider(color = whiteAlpha, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth().clickable {
                                filterState.value = filterState.value.copy(
                                    faculty = faculty,
                                    course = course.toIntOrNull(),
                                    group = group.toIntOrNull(),
                                    isFilterClicked = false
                                )
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.dialog_cancel),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberFilterRow(label: String, placeholder: String, onValueChange: (String) -> Unit) {
    val initVal = if(placeholder == label.lowercase()) {
        ""
    } else {
        placeholder
    }
    var value by remember { mutableStateOf(initVal) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            modifier = Modifier.padding(end = 16.dp)
        )
        TextField(
            value = value,
            onValueChange = {
                value = it
                onValueChange(it)
            },
            placeholder = { Text(placeholder) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = whiteAlpha,
                unfocusedTextColor = whiteAlpha,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedPlaceholderColor = whiteAlpha
            ),
            modifier = Modifier.width(100.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun FilterChip(
    text: String,
    onDismiss: () -> Unit
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        label = { Text(text) },
        selected = enabled,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "",
                modifier = Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = darkGold,
            selectedLabelColor = Color.White,
            selectedTrailingIconColor = Color.White
        )
    )
}

@Composable
fun FilterRowFaculty(
    faculties: List<Faculty>,
    prevFaculty: Faculty?,
    onSelected: (Int) -> Unit
) {
    var selectedFaculty by remember { mutableStateOf<Faculty?>(prevFaculty) }
    var showPopUp by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.dialog_faculty) + ": "
        )
        Box(
            modifier = Modifier.weight(1f).clickable { showPopUp = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (selectedFaculty == null) {
                    stringResource(R.string.dialog_filter_choose) + " " + stringResource(R.string.dialog_faculty).lowercase(
                        Locale.ROOT
                    )
                } else {
                    selectedFaculty!!.title
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        val scrollState = rememberScrollState()
        if (showPopUp) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(
                    excludeFromSystemGesture = true
                ),
                onDismissRequest = { showPopUp = false },
                offset = IntOffset(90, 150)
            ) {
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .heightIn(max = 200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .verticalScroll(scrollState)
                ) {
                    faculties.forEachIndexed { index, item ->
                        if (index != 0) {
                            HorizontalDivider(color = whiteAlpha, modifier = Modifier.fillMaxWidth())
                        }
                        Box(
                            modifier = Modifier
                                .background(darkBlue)
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    showPopUp = !showPopUp
                                    selectedFaculty = item
                                    onSelected(index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.title, color = whiteAlpha)
                        }
                    }
                }
            }
        }
    }
}

private fun makeErrorToast(context: Context, message: String) {
    Toast
        .makeText(context, message, Toast.LENGTH_LONG)
        .show()
}

@Composable
fun ScheduleDialogColumnItem(schedule: Schedule, isSelected: Boolean, onClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            onClicked()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (!isSelected) Arrangement.Start else Arrangement.SpaceBetween
    ) {
        Text(
            text = schedule.title,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.wrapContentWidth().padding(16.dp)
        )
        if (isSelected)
            Icon(
                Icons.Default.Check,
                "Is selected",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
    }
}