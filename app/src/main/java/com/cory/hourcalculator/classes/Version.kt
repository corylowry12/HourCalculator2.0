package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class Version(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the vibration preference
    fun setVersion(state: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Version", state)
        editor.apply()
    }

    // this will load the vibration mode state
    fun loadVersion(): String {
        val state = sharedPreferences.getString("Version", "")
        return (state.toString())
    }
}