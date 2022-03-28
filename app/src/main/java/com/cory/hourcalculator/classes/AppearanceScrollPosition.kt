package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class AppearanceScrollPosition(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setScroll(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("scrollPosition", state)
        editor.apply()
    }

    fun loadScroll(): Int {
        val state = sharedPreferences.getInt("scrollPosition", 0)
        return (state)
    }

    fun setCollapsed(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("appearanceCollapsedToolBar", state)
        editor.apply()
    }

    fun loadCollapsed(): Boolean {
        val state = sharedPreferences.getBoolean("appearanceCollapsedToolBar", false)
        return (state)
    }
}