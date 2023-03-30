package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class LongPressCalculateButtonData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setLongClick(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("long_click", state)
        editor.apply()
    }

    fun loadLongClick(): Boolean {
        val state = sharedPreferences.getBoolean("long_click", true)
        return (state)
    }
}