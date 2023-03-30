package com.cory.hourcalculator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class MenuTintData(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setColoredMenuTint(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("menu_tint", state)
        editor.apply()
    }

    fun loadMenuTint(): Boolean {
        val state = sharedPreferences.getBoolean("menu_tint", true)
        return (state)
    }
}