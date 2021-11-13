package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class DarkThemeData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the theme preference
    fun setDarkModeState(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("DarkTheme", state)
        editor.apply()
    }

    // this will load the night mode state
    fun loadDarkModeState(): Int {
        val state = sharedPreferences.getInt("DarkTheme", 3)
        return (state)
    }
}