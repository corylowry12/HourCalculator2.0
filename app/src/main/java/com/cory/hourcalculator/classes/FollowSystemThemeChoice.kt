package com.cory.hourcalculator.classes

import android.content.Context
import android.content.SharedPreferences

class FollowSystemThemeChoice(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setFollowSystemThemePreference(state: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("FollowSystemThemePreference", state)
        editor.apply()
    }

    fun loadFollowSystemThemePreference(): Int {
        val state = sharedPreferences.getInt("FollowSystemThemePreference", 0)
        return (state)
    }
}