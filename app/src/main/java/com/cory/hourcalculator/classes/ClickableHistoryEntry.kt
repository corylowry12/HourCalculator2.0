package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class ClickableHistoryEntry(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setHistoryItemClickable(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("clickable", state)
        editor.apply()
    }

    fun loadHistoryItemClickable(): Boolean {
        val state = sharedPreferences.getBoolean("clickable", false)
        return (state)
    }

}