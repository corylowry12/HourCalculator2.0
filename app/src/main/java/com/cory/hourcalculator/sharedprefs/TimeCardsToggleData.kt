package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class TimeCardsToggleData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setTimeCardsToggle(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Time Cards Toggle", state)
        editor.apply()
    }

    fun loadTimeCardsState(): Boolean {
        val state = sharedPreferences.getBoolean("Time Cards Toggle", true)
        return (state)
    }
}