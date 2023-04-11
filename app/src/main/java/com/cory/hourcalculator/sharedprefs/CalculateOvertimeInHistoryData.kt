package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class CalculateOvertimeInHistoryData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setCalculateOvertimeInHistoryState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("calculateOvertimeInHistory", state)
        editor.apply()
    }

    fun loadCalculateOvertimeInHistoryState(): Boolean {
        val state = sharedPreferences.getBoolean("calculateOvertimeInHistory", true)
        return (state)
    }
}