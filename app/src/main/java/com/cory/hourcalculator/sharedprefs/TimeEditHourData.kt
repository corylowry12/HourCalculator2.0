package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class TimeEditHourData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setInTimeBool(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("inTimeEditBool", state)
        editor.apply()
    }

    fun loadInTimeBool(): Boolean {
        val state = sharedPreferences.getBoolean("inTimeEditBool", false)
        return (state)
    }

    fun setOutTimeBool(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("outTimeEditBool", state)
        editor.apply()
    }

    fun loadOutTimeBool(): Boolean {
        val state = sharedPreferences.getBoolean("outTimeEditBool", false)
        return (state)
    }

    fun setDateBool(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("dateChangedBool", state)
        editor.apply()
    }

    fun loadDateBool(): Boolean {
        val state = sharedPreferences.getBoolean("dateChangedBool", false)
        return (state)
    }

    fun setBreakTimeBool(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("breakTimeEditBool", state)
        editor.apply()
    }

    fun loadBreakTimeBool(): Boolean {
        val state = sharedPreferences.getBoolean("breakTimeEditBool", false)
        return (state)
    }

    fun setIdMap(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("idMap", state)
        editor.apply()
    }

    fun loadIdMap(): String {
        val state = sharedPreferences.getString("idMap", "")
        return (state!!)
    }

    fun setDate(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("editDate", state)
        editor.apply()
    }

    fun loadDate(): String {
        val state = sharedPreferences.getString("editDate", "")
        return (state!!)
    }
}