package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class FollowSystemVersion(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setSystemColor(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("systemThemeVersion", state)
        editor.apply()
    }

    fun loadSystemColor(): Boolean {
        val state = sharedPreferences.getBoolean("systemThemeVersion", false)
        return (state)
    }
}