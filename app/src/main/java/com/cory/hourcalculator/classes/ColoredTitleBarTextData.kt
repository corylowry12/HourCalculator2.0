package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class ColoredTitleBarTextData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setTitleBarTextState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("coloredTitleBarText", state)
        editor.apply()
    }

    fun loadTitleBarTextState(): Boolean {
        val state = sharedPreferences.getBoolean("coloredTitleBarText", true)
        return (state)
    }
}