package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class HistoryFABPositioning(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setFABPosition(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("History_FAB_Positioning", state)
        editor.apply()
    }

    fun loadFABPosition(): Int {
        val state = sharedPreferences.getInt("History_FAB_Positioning", 1)
        return (state)
    }
}