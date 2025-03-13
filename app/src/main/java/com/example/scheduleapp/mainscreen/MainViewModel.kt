package com.example.scheduleapp.mainscreen

import android.database.SQLException
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.model.Info
import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.services.InfoService
import com.example.scheduleapp.data.services.ScheduleService
import com.example.scheduleapp.data.services.UserService
import com.example.scheduleapp.remote.ScheduleApiService
import com.example.scheduleapp.remote.model.ScheduleResponse
import com.example.scheduleapp.remote.model.StudentRemote
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

@HiltViewModel(assistedFactory = MainViewModel.Factory::class)
class MainViewModel @AssistedInject constructor(
    private val apiService: ScheduleApiService,
    @Assisted val id: UUID
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: UUID): MainViewModel
    }

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    private val _dateState = MutableStateFlow(DateState())
    val dateState = _dateState.asStateFlow()

    private val userService = UserService()
    private val infoService = InfoService()
    private val scheduleService = ScheduleService()

    init {
        loadDates()
        viewModelScope.launch {
            _mainUiState.value = _mainUiState.value.copy(isLoading = true)
            val student = loadStudent()
            val schedules = loadSchedules()
            val studentToSchedule = scheduleService.getStudentToScheduleByStudent(id)
            val mainSchedule = studentToSchedule.find {
                it.isMain
            }
            val currentScheduleIndex =
                if(mainSchedule == null) 0
                else schedules.indexOfFirst { it.scheduleId == mainSchedule.scheduleId }
            _mainUiState.value = _mainUiState.value.copy(
                isLoading = false,
                currentStudent = student,
                usersSchedules = schedules,
                currentSchedule = currentScheduleIndex,
                mainScheduleId = mainSchedule?.scheduleId ?: -1
            )
        }
    }

    fun updateSelectedSchedule(index: Int) {
        _mainUiState.value = _mainUiState.value.copy(currentSchedule = index)
    }

    fun updateSelectedDay(index: Int) {
        _dateState.value = _dateState.value.copy(selectedDay = index)
    }

    private fun loadDates() {
        val calendar = Calendar.getInstance(TimeZone.GMT_ZONE)
        calendar.firstDayOfWeek = Calendar.MONDAY
        val currentDate = calendar.time
        val dates = ArrayList<Date>()
        for (i in 0..5) {
            var day = (2 + i) % 7
            if (day == 0) day = 7
            calendar.set(Calendar.DAY_OF_WEEK, day)
            val time = calendar.time
            dates.add(time)
            Log.d("DATE", DateFormat.barTitleFormat.format(time))
        }
        val currentDateIndex = dates.indexOfFirst {
            DateFormat.barTitleFormat.format(it) == DateFormat.barTitleFormat.format(currentDate)
        }
        Log.d("DATE", currentDateIndex.toString())

        _dateState.value = DateState(dates = dates.toList(), selectedDay = currentDateIndex)
    }

    private suspend fun loadSchedules(): List<Schedule> {
        var studentWithSchedules = scheduleService.getSchedulesForStudent(id)
        Log.d("RESP", studentWithSchedules.toString())
        if (studentWithSchedules != null && studentWithSchedules.schedules.isEmpty()) {
            val scheduleResponse = apiService.getSchedules()
            Log.d("RESP", scheduleResponse.toString())
            mapScheduleResponseAndAddToDb(scheduleResponse, id)
            studentWithSchedules = scheduleService.getSchedulesForStudent(id)
        }
        return studentWithSchedules!!.schedules
    }

    private suspend fun mapScheduleResponseAndAddToDb(scheduleResponse: ScheduleResponse, userId: UUID) {
        scheduleResponse.schedulesItems.forEach { scheduleRem ->
            val scheduleRemote = scheduleRem.schedule
            val schedule = Schedule(
                scheduleId = scheduleRemote.id.toInt(),
                title = scheduleRemote.title,
                lastUpdate = scheduleRemote.lastUpdate.toLong(),
                infoId = scheduleRemote.infoId.toInt()
            )
            scheduleService.insertSchedule(schedule)
            val studentToSchedule = StudentToSchedule(
                userId = userId,
                scheduleId = schedule.scheduleId,
                isMain = scheduleRem.isMain.toBoolean()
            )
            scheduleService.addScheduleToStudent(studentToSchedule)
        }
    }

    private suspend fun loadStudent(): Student? {
        var student = userService.getStudentById(id)
        if (student == null) {
            try {
                val studentResponse = apiService.getStudent(id)
                val info = studentResponseToInfo(studentResponse)
                try {
                    infoService.insertInfo(info)
                } catch(e: SQLException) {
                    Log.d("INFOSQL", "This info is added")
                }
                student = studentResponseToStudentModel(studentResponse, info)
                userService.insertStudent(student)
            } catch(e: Exception) {
                Log.d("MAINSCREEN", e.message.toString())
            }
        }
        return student
    }
    private fun studentResponseToInfo(studentRemote: StudentRemote): Info =
        Info(
            id = studentRemote.info.id.toInt(),
            facultyId = studentRemote.info.facultyId.toInt(),
            specialization = studentRemote.info.specialization,
            course = studentRemote.info.course.toInt(),
            group = studentRemote.info.group.toInt()
        )
    private fun studentResponseToStudentModel(studentRemote: StudentRemote, info: Info): Student =
        Student(
            userId = UUID.fromString(studentRemote.userId),
            name = studentRemote.name,
            infoId = info.id
        )
}