package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class ClearBreakTextAutomaticallyData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setClearAutomatically(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("clearAutomatically", state)
        editor.apply()
    }

    fun loadClearAutomatically(): Boolean {
        val state = sharedPreferences.getBoolean("clearAutomatically", true)
        return (state)
    }
}