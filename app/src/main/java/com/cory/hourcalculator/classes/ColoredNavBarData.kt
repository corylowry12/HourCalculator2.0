package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class ColoredNavBarData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setNavBar(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("ColoredNavBar", state)
        editor.apply()
    }

    fun loadNavBar(): Boolean {
        val state = sharedPreferences.getBoolean("ColoredNavBar", false)
        return (state)
    }
}