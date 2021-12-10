package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class ColoredNavBarData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    //this saves the theme preference
    fun setNavBar(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("ColoredNavBar", state)
        editor.apply()
    }

    // this will load the night mode state
    fun loadNavBar(): Boolean {
        val state = sharedPreferences.getBoolean("ColoredNavBar", false)
        return (state)
    }
}