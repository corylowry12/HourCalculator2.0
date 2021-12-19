package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class IdData(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setID(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("id", state)
        editor.apply()
    }

    fun loadID(): Int {
        val state = sharedPreferences.getInt("id", 0)
        return (state)
    }
}