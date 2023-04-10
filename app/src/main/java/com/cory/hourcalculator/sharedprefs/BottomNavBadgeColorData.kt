package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class BottomNavBadgeColorData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setColor(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("bottomNavBadgeColor", state)
        editor.apply()
    }

    fun loadColor(): Boolean {
        val state = sharedPreferences.getBoolean("bottomNavBadgeColor", true)
        return (state)
    }
}