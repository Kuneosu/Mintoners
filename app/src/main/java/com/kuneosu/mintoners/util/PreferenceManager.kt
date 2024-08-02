package com.kuneosu.mintoners.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val PREF_NAME = "my_preferences"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setFirstTimeLaunch(fragmentName: String, isFirstTime: Boolean) {
        editor.putBoolean("${fragmentName}_first_time", isFirstTime)
        editor.commit()
    }

    fun isFirstTimeLaunch(fragmentName: String): Boolean {
        return sharedPreferences.getBoolean("${fragmentName}_first_time", true)
    }

    fun resetAllPreferences() {
        editor.clear()
        editor.commit()
    }
}