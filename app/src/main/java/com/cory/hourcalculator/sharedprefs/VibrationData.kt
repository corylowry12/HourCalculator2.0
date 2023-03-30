package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class VibrationData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setVibrationState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Vibration", state)
        editor.apply()
    }

    fun loadVibrationState(): Boolean {
        val state = sharedPreferences.getBoolean("Vibration", true)
        return (state)
    }
}