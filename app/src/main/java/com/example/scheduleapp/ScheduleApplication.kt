package com.example.scheduleapp

import android.app.Application
import com.example.scheduleapp.data.repository.AppRepository

class ScheduleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepository.init(this.applicationContext)
    }
}