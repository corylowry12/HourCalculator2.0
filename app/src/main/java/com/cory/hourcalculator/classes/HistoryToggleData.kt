package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class HistoryToggleData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setHistoryToggle(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("History Toggle", state)
        editor.apply()
    }

    fun loadHistoryState(): Boolean {
        val state = sharedPreferences.getBoolean("History Toggle", true)
        return (state)
    }
}