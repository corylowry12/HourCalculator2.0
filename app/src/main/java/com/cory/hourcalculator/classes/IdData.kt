package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class IdData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the theme preference
    fun setID(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("id", state)
        editor.apply()
    }

    // this will load the night mode state
    fun loadID(): Int {
        val state = sharedPreferences.getInt("id", -1)
        return (state)
    }
}