package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class OutTimeData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setOutTimeState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("outTimeState", state)
        editor.apply()
    }

    fun loadOutTimeState(): Boolean {
        val state = sharedPreferences.getBoolean("outTimeState", false)
        return (state)
    }
}