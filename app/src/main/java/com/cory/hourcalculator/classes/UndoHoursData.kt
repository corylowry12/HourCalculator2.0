package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.cory.hourcalculator.R
import com.google.android.material.textfield.TextInputEditText

class UndoHoursData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the vibration preference
    fun setID(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("id", state)
        editor.apply()
    }

    // this will load the vibration mode state
    fun loadID(): Int {
        val state = sharedPreferences.getInt("id", 0)
        return (state)
    }

    //this saves the vibration preference
    fun setInTime(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("inTime", state)
        editor.apply()
    }

    // this will load the vibration mode state
    fun loadInTime(): String {
        val state = sharedPreferences.getString("inTime", "")
        return (state.toString())
    }

    //this saves the vibration preference
    fun setOutTime(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("outTime", state)
        editor.apply()
    }

    // this will load the vibration mode state
    fun loadOutTime(): String {
        val state = sharedPreferences.getString("outTime", "")
        return (state.toString())
    }

    //this saves the vibration preference
    fun setBreakTime(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("breakTime", state)
        editor.apply()
    }

    // this will load the vibration mode state
    fun loadBreakTime(): String {
        val state = sharedPreferences.getString("breakTime", "")
        return (state.toString())
    }

    //this saves the vibration preference
    fun setDate(state: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("date", state)
        editor.apply()
    }

    // this will load the vibration mode state
    fun loadDate(): Long {
        val state = sharedPreferences.getLong("date", 0L)
        return (state)
    }

    //this saves the vibration preference
    fun setTotalHours(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("total", state)
        editor.apply()
    }

    // this will load the vibration mode state
    fun loadTotalHours(): String {
        val state = sharedPreferences.getString("total", "")
        return (state.toString())
    }
}