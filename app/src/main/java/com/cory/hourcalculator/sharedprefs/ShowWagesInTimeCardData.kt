package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class ShowWagesInTimeCardData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setShowWages(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("showWagesInTimeCards", state)
        editor.apply()
    }

    fun loadShowWages(): Boolean {
        val state = sharedPreferences.getBoolean("showWagesInTimeCards", false)
        return (state)
    }
}