package com.example.nepsis.core.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("nepsis_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, userId: String) {
        prefs.edit().apply {
            putString("ACCESS_TOKEN", token)
            putString("USER_ID", userId)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString("ACCESS_TOKEN", null)
    
    fun getUserId(): String? = prefs.getString("USER_ID", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getToken() != null && getUserId() != null
}
