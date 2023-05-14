package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class ShowBreakTimeInDecimalData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setShowBreakTimeInDecimal(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("showBreakTimeInDecimal", state)
        editor.apply()
    }

    fun loadShowBreakTimeInDecimal(): Boolean {
        val state = sharedPreferences.getBoolean("showBreakTimeInDecimal", false)
        return (state)
    }
}