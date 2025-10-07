package com.example.salonbookingapp.utils

import android.content.Context
import com.example.salonbookingapp.data.User
import com.google.gson.Gson

class PrefManager(context: Context) {

    private val prefs = context.getSharedPreferences("salon_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_USER = "key_user"
        private const val KEY_LOGGED_IN = "key_logged_in"
    }

    /** Save a full User object to SharedPreferences */
    fun saveUser(user: User) {
        val json = gson.toJson(user)
        prefs.edit().putString(KEY_USER, json).apply()
        setLoggedIn(true)
    }

    /** Retrieve the saved User object */
    fun getUser(): User? {
        val json = prefs.getString(KEY_USER, null) ?: return null
        return try {
            gson.fromJson(json, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /** Set login flag */
    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, isLoggedIn).apply()
    }

    /** Check if user is logged in */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    /** Log out user */
    fun logout() {
        prefs.edit().clear().apply()
    }
}
