package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class DateData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setMinutes1(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Spinner1Minutes", state)
        editor.apply()
    }

    fun loadMinutes1(): String? {
        val state = sharedPreferences.getString("Spinner1Minutes", "")
        return (state)
    }

    fun setHours1(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Spinner1Hours", state)
        editor.apply()
    }

    fun loadHours1(): String? {
        val state = sharedPreferences.getString("Spinner1Hours", "")
        return (state)
    }

    fun setMinutes2(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Spinner2Minutes", state)
        editor.apply()
    }

    fun loadMinutes2(): String? {
        val state = sharedPreferences.getString("Spinner2Minutes", "")
        return (state)
    }

    fun setHours2(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Spinner2Hours", state)
        editor.apply()
    }

    fun loadHours2(): String? {
        val state = sharedPreferences.getString("Spinner2Hours", "")
        return (state)
    }
}