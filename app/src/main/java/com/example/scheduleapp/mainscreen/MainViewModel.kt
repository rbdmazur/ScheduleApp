package com.example.scheduleapp.mainscreen

import android.database.SQLException
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.model.Faculty
import com.example.scheduleapp.data.model.Info
import com.example.scheduleapp.data.model.Notification
import com.example.scheduleapp.data.model.NotificationData
import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.model.StudentWithEmailAndInfo
import com.example.scheduleapp.data.model.Study
import com.example.scheduleapp.data.model.StudyWithTeacherAndSubject
import com.example.scheduleapp.data.model.Subject
import com.example.scheduleapp.data.model.Teacher
import com.example.scheduleapp.data.services.InfoService
import com.example.scheduleapp.data.services.NotificationService
import com.example.scheduleapp.data.services.ScheduleService
import com.example.scheduleapp.data.services.UserService
import com.example.scheduleapp.mainscreen.adddialog.DialogState
import com.example.scheduleapp.mainscreen.adddialog.ScheduleLazyColumnItem
import com.example.scheduleapp.remote.ScheduleApiService
import com.example.scheduleapp.remote.model.ScheduleResponse
import com.example.scheduleapp.remote.model.ScheduleSimpleResponse
import com.example.scheduleapp.remote.model.StudentRemote
import com.example.scheduleapp.remote.model.StudyRemote
import com.example.scheduleapp.remote.model.SubjectRemote
import com.example.scheduleapp.remote.model.TeacherRemote
import com.example.scheduleapp.remote.requests.ScheduleRequest
import com.example.scheduleapp.remote.requests.ScheduleRequestList
import com.example.scheduleapp.utils.DateFormats
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState = _dialogState.asStateFlow()

    private val _faculties = MutableStateFlow(emptyList<Faculty>())
    val faculties = _faculties.asStateFlow()

    private val _networkConnection = MutableStateFlow(false)
    val networkConnection = _networkConnection.asStateFlow()

    private val _subjects = MutableStateFlow(emptyList<Subject>())
    val subjects = _subjects.asStateFlow()

    private val _notifications = MutableStateFlow(emptyList<Notification>())
    val notifications = _notifications.asStateFlow()

    private val _studentInfo = MutableStateFlow<StudentWithEmailAndInfo?>(null)
    val studentInfo = _studentInfo.asStateFlow()

    private val userService = UserService()
    private val infoService = InfoService()
    private val scheduleService = ScheduleService()
    private val notificationService = NotificationService()

    init {
        loadDates()
        viewModelScope.launch {
            try {
                loadData()
            } catch (e: HttpException) {
                Log.d("MVM", "Code: ${e.code()}")
            }

        }
    }

    private suspend fun loadData() {
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
        val studies = loadStudies(schedules[currentScheduleIndex].scheduleId)
        _mainUiState.value = _mainUiState.value.copy(
            isLoading = false,
            currentStudent = student,
            usersSchedules = schedules,
            currentScheduleIndex = currentScheduleIndex,
            mainScheduleId = mainSchedule?.scheduleId ?: -1,
            currentStudies = studies
        )
    }

    fun loadStudentInfo() {
        viewModelScope.launch {
            val student = _mainUiState.value.currentStudent
            student?.let {
                _studentInfo.value = userService.getStudentWithInfo(it.userId)
            }
        }
    }

    fun loadSubjects() {
        viewModelScope.launch {
            val currentSt = _mainUiState.value.currentStudent
            if (currentSt != null) {
                _subjects.value = scheduleService.getAllSubjectsByInfoId(currentSt.infoId)
            }
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            val currentSt = _mainUiState.value.currentStudent
            if (currentSt != null) {
                val notif = notificationService.getAllNotifications(currentSt.userId)
                _notifications.value = notif.sortedWith { n1: Notification, n2: Notification ->
                    val date1 = Date(n1.date)
                    val date2 = Date(n2.date)
                    date1.compareTo(date2)
                }
            }
        }
    }

    fun addNotification(subject: Subject, title: String, date: Long) {
        viewModelScope.launch {
            val currentSt = _mainUiState.value.currentStudent
            if (currentSt != null) {
                notificationService.insertNotification(
                    NotificationData(
                        studentId = currentSt.userId,
                        subjectId = subject.id,
                        title = title,
                        description = "",
                        date = date
                    )
                )
            }
        }
    }

    fun setNetworkState(state: Boolean) {
        _networkConnection.value = state
        if (state) {
            viewModelScope.launch {
                loadData()
            }
        }
    }

    fun loadFaculties() {
        viewModelScope.launch {
            _faculties.value = apiService
                .getFaculties()
                .faculties
                .map { Faculty(it.id.toInt(), it.title, it.fullTitle) }
        }
    }

    fun removeSchedule(schedule: Schedule) {
        viewModelScope.launch {
            val toDelete = StudentToSchedule(
                userId = mainUiState.value.currentStudent!!.userId,
                scheduleId = schedule.scheduleId,
                isMain = false
            )
            try {
                val newSchedules = _mainUiState.value.usersSchedules.filter { it.scheduleId != schedule.scheduleId }
                _mainUiState.value = _mainUiState.value.copy(
                    usersSchedules = newSchedules
                )
                updateSelectedSchedule(0)
                scheduleService.deleteScheduleFromStudent(toDelete)
                apiService.deleteScheduleFromStudent(scheduleId = schedule.scheduleId)
            } catch (e: HttpException) {
                Log.d("DELETE", e.message())
            } catch (e: Exception) {
                Log.d("DELETE", e.message.toString())
            }
        }
    }

    fun updateSelectedSchedule(index: Int) {
        viewModelScope.launch {
            val currentScheduleId = _mainUiState.value.usersSchedules[index].scheduleId
            _mainUiState.value = _mainUiState.value.copy(
                currentScheduleIndex = index,
                currentStudies = loadStudies(currentScheduleId)
            )
        }
    }

    fun addSelectedSchedules(schedules: List<Schedule>) {
        viewModelScope.launch {
            val result = ArrayList<Schedule>()
            _mainUiState.value.usersSchedules.forEach {
                result.add(it)
            }

            val request = ScheduleRequestList(
                schedules.map { ScheduleRequest(it.scheduleId.toString(), false) }
            )

            apiService.addSchedulesToStudent(
                id = _mainUiState.value.currentStudent!!.userId,
                schedules = request
            )

            schedules.forEach {
                insert {
                    scheduleService.insertSchedule(it)
                }
                scheduleService.addScheduleToStudent(
                    StudentToSchedule(
                        userId = _mainUiState.value.currentStudent!!.userId,
                        scheduleId = it.scheduleId,
                        isMain = false
                    )
                )
                result.add(it)
            }

            _mainUiState.value = _mainUiState.value.copy(usersSchedules = result)
        }
    }

    fun updateSelectedDay(index: Int) {
        _dateState.value = _dateState.value.copy(selectedDay = index)
    }

    fun loadSchedulesForFilter(facultyId: Int?, course: Int?, group: Int?) {
        if (_mainUiState.value.currentStudent == null) {
            throw IllegalStateException()
        }
        viewModelScope.launch {
            val response = apiService.getAvailableSchedules(
                id = _mainUiState.value.currentStudent!!.userId,
                facultyId = facultyId,
                course = course,
                group = group
            )
            val list = mapScheduleSimpleResponseToSchedule(response).map {
                ScheduleLazyColumnItem(schedule = it)
            }
            _dialogState.value = _dialogState.value.copy(list = list)
        }
    }

    fun updateDialogSelectedSchedule(list: List<ScheduleLazyColumnItem>) {
        _dialogState.value = _dialogState.value.copy(list = list)
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
            Log.d("DATE", DateFormats.barTitleFormat.format(time))
        }
        val currentDateIndex = dates.indexOfFirst {
            DateFormats.barTitleFormat.format(it) == DateFormats.barTitleFormat.format(currentDate)
        }
        Log.d("DATE", currentDateIndex.toString())

        _dateState.value = DateState(dates = dates.toList(), selectedDay = currentDateIndex)
    }

    private suspend fun loadStudies(scheduleId: Int): List<StudyWithTeacherAndSubject> {
        var studies = scheduleService.getStudiesWithSubjectAndTeacherForSchedule(scheduleId)
        if (networkConnection.value) {
            try {
                val studiesResponse = apiService.getStudiesForSchedule(scheduleId)
                studiesResponse.studies.forEach {
                    val subject = subjectFromSubjectRemote(it.subject)
                    val teacher = teacherFromTeacherRemote(it.teacher)
                    val study = studyFromStudyRemote(it)

                    insert {
                        scheduleService.insertSubject(subject)
                    }
                    insert {
                        userService.insertTeacher(teacher)
                    }
                    insert {
                        scheduleService.insertStudy(study)
                    }

                    studies = scheduleService.getStudiesWithSubjectAndTeacherForSchedule(scheduleId)
                }
            } catch(e: HttpException) {
                Log.d("STUDY", e.message())
            }
        }
        return studies
    }

    private suspend fun loadSchedules(): List<Schedule> {
        var studentWithSchedules = scheduleService.getSchedulesForStudent(id)
        Log.d("RESP", studentWithSchedules.toString())
        if (networkConnection.value) {
            val scheduleResponse = apiService.getSchedules()
            Log.d("RESP", scheduleResponse.toString())
            mapScheduleResponseAndAddToDb(scheduleResponse, id)
            studentWithSchedules = scheduleService.getSchedulesForStudent(id)
        }
        return studentWithSchedules!!.schedules
    }

    private fun mapScheduleSimpleResponseToSchedule(response: ScheduleSimpleResponse): List<Schedule> {
        val result = ArrayList<Schedule>()
        response.schedules.forEach {
            val schedule = Schedule(
                scheduleId = it.id.toInt(),
                title = it.title,
                infoId = it.infoId.toInt(),
                lastUpdate = it.lastUpdate.toLong()
            )
            result.add(schedule)
        }
        return result.toList()
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
            insert {
                scheduleService.insertSchedule(schedule)
            }
            val studentToSchedule = StudentToSchedule(
                userId = userId,
                scheduleId = schedule.scheduleId,
                isMain = scheduleRem.isMain.toBoolean()
            )
            insert {
                scheduleService.addScheduleToStudent(studentToSchedule)
            }
        }
    }

    private suspend fun loadStudent(): Student? {
        var student = userService.getStudentById(id)
        if (networkConnection.value) {
            try {
                val studentResponse = apiService.getStudent(id)
                val info = studentResponseToInfo(studentResponse)
                insert {
                    infoService.insertInfo(info)
                }
                student = studentResponseToStudentModel(studentResponse, info)
                insert {
                    userService.insertStudent(student)
                }
            } catch(e: Exception) {
                Log.d("MAINSCREEN", e.message.toString())
            }
        }
        return student
    }

    private suspend fun insert(action: suspend () -> Unit) {
        try {
            action()
        } catch (e: SQLException) {
            Log.d("SQL", "This object is added")
        }
    }

    private fun subjectFromSubjectRemote(remote: SubjectRemote): Subject =
        Subject(
            id = remote.id.toInt(),
            title = remote.title,
            shortTitle = remote.shortTitle,
            infoId = remote.infoId.toInt()
        )

    private fun teacherFromTeacherRemote(remote: TeacherRemote): Teacher =
        Teacher(
            userId = UUID.fromString(remote.userId),
            name = remote.name,
            academicTitle = remote.academicTitle,
            facultyId = remote.facultyId.toInt()
        )

    private fun studyFromStudyRemote(remote: StudyRemote): Study =
        Study(
            id = remote.id.toInt(),
            subjectId = remote.subject.id.toInt(),
            day = remote.day,
            number = remote.number.toInt(),
            time = remote.time,
            type = remote.type,
            auditorium = remote.auditorium,
            teacherId = UUID.fromString(remote.teacher.userId),
            scheduleId = remote.scheduleId.toInt()
        )

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