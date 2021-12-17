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

    fun setID(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("id", state)
        editor.apply()
    }

    fun loadID(): Int {
        val state = sharedPreferences.getInt("id", 0)
        return (state)
    }

    fun setInTime(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("inTime", state)
        editor.apply()
    }

    fun loadInTime(): String {
        val state = sharedPreferences.getString("inTime", "")
        return (state.toString())
    }

    fun setOutTime(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("outTime", state)
        editor.apply()
    }

    fun loadOutTime(): String {
        val state = sharedPreferences.getString("outTime", "")
        return (state.toString())
    }

    fun setBreakTime(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("breakTime", state)
        editor.apply()
    }

    fun loadBreakTime(): String {
        val state = sharedPreferences.getString("breakTime", "")
        return (state.toString())
    }

    fun setDate(state: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("date", state)
        editor.apply()
    }

    fun loadDate(): Long {
        val state = sharedPreferences.getLong("date", 0L)
        return (state)
    }

    fun setTotalHours(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("total", state)
        editor.apply()
    }

    fun loadTotalHours(): String {
        val state = sharedPreferences.getString("total", "")
        return (state.toString())
    }
}