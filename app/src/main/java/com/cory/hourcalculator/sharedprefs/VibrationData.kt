package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class VibrationData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setVibrationOnClickState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Vibration", state)
        editor.apply()
    }

    fun loadVibrationOnClickState(): Boolean {
        val state = sharedPreferences.getBoolean("Vibration", true)
        return (state)
    }

    fun setVibrationOnTimePickerChangeState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("VibrationOnTimePickerChange", state)
        editor.apply()
    }

    fun loadVibrationOnTimePickerChangeState(): Boolean {
        val state = sharedPreferences.getBoolean("VibrationOnTimePickerChange", true)
        return (state)
    }

    fun setVibrationOnLongClickState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("VibrationOnLongClick", state)
        editor.apply()
    }

    fun loadVibrationOnLongClickState(): Boolean {
        val state = sharedPreferences.getBoolean("VibrationOnLongClick", false)
        return (state)
    }

    fun setVibrationOnErrorState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("VibrationOnError", state)
        editor.apply()
    }

    fun loadVibrationOnErrorState(): Boolean {
        val state = sharedPreferences.getBoolean("VibrationOnError", true)
        return (state)
    }
}