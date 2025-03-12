package com.example.scheduleapp.remote.auth

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.scheduleapp.R

class SessionManager(app: Application) {
    private var prefs: SharedPreferences =
        app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }

    fun saveAuthToken(token: String, id: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_ID, id)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }
}