package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class BreakTextBoxVisibilityClass(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setVisibility(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("visibility", state)
        editor.apply()
    }

    fun loadVisiblity(): Int {
        val state = sharedPreferences.getInt("visibility", 0)
        return (state)
    }
}