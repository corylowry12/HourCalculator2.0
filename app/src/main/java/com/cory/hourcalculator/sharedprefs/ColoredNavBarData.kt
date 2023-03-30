package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class ColoredNavBarData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setNavBar(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("ColoredNavBarData", state)
        editor.apply()
    }

    fun loadNavBar(): Boolean {
        val state = sharedPreferences.getBoolean("ColoredNavBarData", true)
        return (state)
    }
}